package data.repository

import data.remote.Api
import data.remote.ApiResponseHandler
import data.remote.DataState
import data.remote.dto.RatesMapper
import domain.model.Rate
import domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import main.ApplicationClass
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RepositoryImpl @Inject constructor(
    app: ApplicationClass,
    private val api: Api,
    private val ratesMapper: RatesMapper,
) : ApiResponseHandler(app), Repository {
    override suspend fun getExchangeRates(): DataState<ArrayList<Rate>> {
        return when (val response = call(api.getExchangeRates())) {
            is DataState.Loading -> response
            is DataState.Failure -> response
            is DataState.Success -> {
                val result = ratesMapper.toDomainModel(response.data.rates)
                DataState.Success(result)
            }
        }
    }
}