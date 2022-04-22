package domain.usecase.convert

import domain.model.ConvertResult
import domain.model.Rate
import domain.repository.Repository
import main.ApplicationClass
import util.Constant
import javax.inject.Inject

class ApplyConvertUseCase @Inject constructor(private val app: ApplicationClass, private val repository: Repository) {
    suspend operator fun invoke(convertResult: ConvertResult, balanceList: ArrayList<Rate>): ArrayList<Rate> {
        balanceList[0].value = balanceList[0].value - convertResult.sellFee - convertResult.sellValue
        balanceList[1].value = balanceList[1].value - convertResult.receiveFee + convertResult.receiveValue
        repository.updateBalanceList(balanceList)
        if (app.appSetting.freeConvertCount > 0) {
            app.appSetting.freeConvertCount--// = appSetting.freeConvertCount-1
        }
        app.prefManager.setPref(Constant.PREF_CONVERT_COUNT, app.prefManager.getIntPref(Constant.PREF_CONVERT_COUNT) + 1)
        return balanceList
    }
}