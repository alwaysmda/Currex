package data.remote

import com.google.gson.Gson
import domain.model.NetworkError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import main.ApplicationClass
import retrofit2.Response


abstract class ApiResponseHandler(private val app: ApplicationClass) {
    suspend fun <T> call(response: Response<T>): Flow<DataState<T>> = flow {
        try {
            emit(DataState.Loading)
            delay(1000)
            if (response.isSuccessful) {
                emit(DataState.Success(response.body()!!))
            } else {
                response.errorBody()?.let {
                    try {
                        //todo parse error
                        val error = Gson().fromJson(it.string(), NetworkError::class.java)
                        emit(DataState.Failure(error))
                    } catch (e: Exception) {
                        emit(DataState.Failure(NetworkError(response.code(), app.m.somethingWentWrong, e)))
                    }
                } ?: kotlin.run {
                    emit(DataState.Failure(NetworkError(response.code(), app.m.somethingWentWrong, null)))
                }
            }
        } catch (e: Exception) {
            emit(DataState.Failure(NetworkError(0, app.m.checkConnectionAndTryAgain, e)))
        }
    }
}