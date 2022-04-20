package data.repository

import data.remote.Api
import data.remote.ApiResponseHandler
import data.remote.DataState
import data.remote.dto.NetworkErrorMapper
import data.remote.dto.ResponseExchangeRateMapper
import domain.model.ResponseExchangeRate
import domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RepositoryImpl @Inject constructor(
    private val app: ApplicationClass,
    private val api: Api,
    private val networkErrorMapper: NetworkErrorMapper,
    private val responseExchangeRateMapper: ResponseExchangeRateMapper,
) : ApiResponseHandler(app, networkErrorMapper), Repository {
    override suspend fun getExchangeRates(): DataState<ResponseExchangeRate> {
        return when (val response = call(api.getExchangeRates())) {
            is DataState.Loading -> response
            is DataState.Failure -> response
            is DataState.Success -> {
                val result = responseExchangeRateMapper.toDomainModel(response.data)
                DataState.Success(result)
            }
        }
    }
}