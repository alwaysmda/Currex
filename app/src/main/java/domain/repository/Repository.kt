package domain.repository

import data.remote.DataState
import domain.model.ResponseExchangeRate

interface Repository {
    suspend fun getExchangeRates(): DataState<ResponseExchangeRate>
}