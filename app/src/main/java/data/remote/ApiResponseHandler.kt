package data.remote

import com.google.gson.Gson
import com.google.gson.JsonParser
import data.remote.dto.NetworkErrorDto
import data.remote.dto.NetworkErrorMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import main.ApplicationClass
import okhttp3.ResponseBody
import retrofit2.Response
import util.extension.log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


abstract class
ApiResponseHandler(
    private val app: ApplicationClass,
    private val networkErrorMapper: NetworkErrorMapper
) {
    fun <T> call(response: Response<T>): DataState<T> {
        try {
            if (response.isSuccessful) {
                return DataState.Success(response.body()!!)
            } else {
                response.errorBody()?.let {
                    return try {
                        val error = Gson().fromJson(JsonParser().parse(it.string()), NetworkErrorDto::class.java)
                        DataState.Failure(error.meta.code, error.meta.message, networkErrorMapper.toDomainModel(error))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        DataState.Failure(response.code(), app.m.somethingWentWrong)
                    }
                } ?: kotlin.run {
                    return DataState.Failure(response.code(), app.m.somethingWentWrong)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DataState.Failure(DataState.Failure.CODE_NO_INTERNET, app.m.checkConnectionAndTryAgain)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun call(path: String, name: String, response: Response<ResponseBody>, data: String?) = channelFlow {
        send(DownloadDataState.Loading)
        if (response.isSuccessful) {
            downloadResponse(response.body() as ResponseBody, path, name, data).collectLatest {
                send(it)
            }
        } else {
            send(DownloadDataState.Failure(0, ""))
        }
    }

    private fun downloadResponse(body: ResponseBody, path: String, name: String, data: String?) = flow {
        kotlin.runCatching {
            try {
                val directory = File(path)
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val file = File(path, name)
                if (file.exists()) {
                    file.delete()
                }

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null
                try {
                    val fileReader = ByteArray(4096)
                    val fileSize = body.contentLength()
                    var fileSizeDownloaded: Long = 0
                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(file)
                    while (true) {
                        val read: Int = inputStream.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()
                        val percent = (fileSizeDownloaded * 100F) / fileSize
                        emit(DownloadDataState.Downloading(percent.toInt()))
                        log("file download: $fileSizeDownloaded of $fileSize $percent%")
                    }
                    outputStream.flush()
                    emit(DownloadDataState.Success(path, name, data))
                } catch (e: Exception) {
                    emit(DownloadDataState.Failure(0, ""))
                } finally {
                    inputStream?.close()
                    outputStream?.close()
                }
            } catch (e: Exception) {
                emit(DownloadDataState.Failure(0, ""))
            }
        }.onFailure {
            emit(DownloadDataState.Failure(0, ""))
        }
    }
}