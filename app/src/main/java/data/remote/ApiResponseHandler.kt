package data.remote

import com.example.currex.R
import main.ApplicationClass
import retrofit2.Response


abstract class ApiResponseHandler(
    private val app: ApplicationClass,
) {
   suspend fun <T> call(request: suspend () -> (Response<T>)): DataState<T> {
       try {
           val response = request()
           if (response.isSuccessful) {
               return DataState.Success(response.body()!!)
           } else {
               response.errorBody()?.let {
                   return DataState.Failure(response.code(), app.getString(R.string.something_went_wrong))
               } ?: kotlin.run {
                   return DataState.Failure(response.code(), app.getString(R.string.something_went_wrong))
               }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return DataState.Failure(DataState.Failure.CODE_NO_INTERNET, app.getString(R.string.no_internet_connection))
        }
    }
}