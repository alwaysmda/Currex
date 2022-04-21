package domain.usecase.convert

import data.remote.DataState
import domain.model.Rate
import domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<DataState<ArrayList<Rate>>> = flow {
        emit(DataState.Loading)
        val result = repository.getExchangeRates()
        emit(result)
    }
}