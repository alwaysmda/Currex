package domain.usecase.convert

import domain.repository.Repository
import util.AppSetting
import javax.inject.Inject


class ResetBalanceListUseCase @Inject constructor(private val appSetting: AppSetting, private val repository: Repository) {
    suspend operator fun invoke() {
        val balanceList = repository.getBalanceList()
        balanceList.forEach {
            it.value = if (it.name == appSetting.initialBalanceName) appSetting.initialBalanceValue else 0.0
        }
        repository.updateBalanceList(balanceList)
    }
}