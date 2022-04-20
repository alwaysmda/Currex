package data.remote

import data.remote.dto.ResponseExchangeRateDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import util.Constant

interface Api {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query(value = "access_key", encoded = true) access_key: String = Constant.CON_API_ACCESS_KEY
    ): Response<ResponseExchangeRateDto>
}