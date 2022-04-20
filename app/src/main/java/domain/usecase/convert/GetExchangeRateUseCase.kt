package domain.usecase.convert

import data.remote.DataState
import domain.model.ResponseExchangeRate
import domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<DataState<ResponseExchangeRate>> = flow {
        emit(DataState.Loading)
        val result = repository.getExchangeRates()
        emit(result)
    }
}