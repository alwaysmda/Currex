package domain.repository

import data.remote.DataState
import domain.model.Rate

interface Repository {
    suspend fun getExchangeRates(): DataState<ArrayList<Rate>>
}