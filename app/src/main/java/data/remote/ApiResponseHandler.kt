package data.remote

import com.google.gson.Gson
import domain.model.NetworkError
import main.ApplicationClass
import retrofit2.Response


abstract class ApiResponseHandler(private val app: ApplicationClass) {
    fun <T> call(response: Response<T>): DataState<T> {
        try {
            if (response.isSuccessful) {
                return DataState.Success(response.body()!!)
            } else {
                response.errorBody()?.let {
                    return try {
                        //todo parse error
                        val error = Gson().fromJson(it.string(), NetworkError::class.java)
                        DataState.Failure(error)
                    } catch (e: Exception) {
                        DataState.Failure(NetworkError(response.code(), app.m.somethingWentWrong, e))
                    }
                } ?: kotlin.run {
                    return DataState.Failure(NetworkError(response.code(), app.m.somethingWentWrong, null))
                }
            }
        } catch (e: Exception) {
            return DataState.Failure(NetworkError(0, app.m.checkConnectionAndTryAgain, e))
        }
    }
}