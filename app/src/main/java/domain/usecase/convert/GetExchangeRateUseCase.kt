package domain.usecase.convert

import data.remote.DataState
import domain.model.Rate
import domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import util.AppSetting
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(private val appSetting: AppSetting, private val repository: Repository) {
    operator fun invoke(): Flow<DataState<ArrayList<Rate>>> = flow {
        emit(DataState.Loading)
        val result = repository.getExchangeRates()
        if (result is DataState.Success) {
            val balanceList = repository.getBalanceList()
            if (balanceList.isEmpty()) {
                balanceList.add(Rate(appSetting.initialBalanceName, appSetting.initialBalanceValue))
            }
            balanceList.addAll(result.data.filter { it.name !in balanceList.map { item -> item.name }.toSet() }.map { Rate(it.name, 0.0) })
            repository.updateBalanceList(balanceList)
        }
        emit(result)
    }
}