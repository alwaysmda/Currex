package domain.usecase.convert

import domain.model.ConvertResult
import domain.model.Rate
import domain.repository.Repository
import util.Constant
import util.PrefManager
import javax.inject.Inject

class ApplyConvertUseCase @Inject constructor(private val prefManager: PrefManager, private val repository: Repository) {
    suspend operator fun invoke(convertResult: ConvertResult, balanceList: ArrayList<Rate>): ArrayList<Rate> {
        balanceList[0].value = balanceList[0].value - convertResult.sellFee - convertResult.sellValue
        balanceList[1].value = balanceList[1].value - convertResult.receiveFee + convertResult.receiveValue
        repository.updateBalanceList(balanceList)
        val remainingFreeConversion = prefManager.getIntPref(Constant.PREF_FREE_CONVERT_COUNT)
        if (remainingFreeConversion > 0) {
            prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, remainingFreeConversion - 1)
        }
        return balanceList
    }
}