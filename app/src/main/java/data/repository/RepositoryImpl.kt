package data.repository

import data.db.RateDao
import data.remote.Api
import data.remote.ApiResponseHandler
import data.remote.DataState
import data.remote.dto.RatesMapper
import domain.model.Rate
import domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: Api,
    private val rateDao: RateDao,
    private val ratesMapper: RatesMapper,
) : ApiResponseHandler(), Repository {
    override suspend fun getExchangeRates(): DataState<ArrayList<Rate>> {
        return when (val response = call { api.getExchangeRates() }) {
            is DataState.Loading -> response
            is DataState.Failure -> response
            is DataState.Success -> {
                val result = ratesMapper.toDomainModel(response.data.rates)
                DataState.Success(result)
            }
        }
    }

    override suspend fun getBalanceList(): ArrayList<Rate> {
        return ArrayList(rateDao.getBalanceList())
    }

    override suspend fun updateBalanceList(list: ArrayList<Rate>) {
        rateDao.upsertAll(list)
    }
}