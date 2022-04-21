package domain.usecase.convert

import com.google.gson.Gson
import domain.model.ConvertResult
import domain.model.Rate
import main.ApplicationClass
import util.Constant
import javax.inject.Inject

class ApplyConvertUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(convertResult: ConvertResult, balanceList: ArrayList<Rate>): ArrayList<Rate> {
        balanceList[0].value = balanceList[0].value - convertResult.sellFee - convertResult.sellValue
        balanceList[1].value = balanceList[1].value - convertResult.receiveFee + convertResult.receiveValue
        app.prefManager.setPref(Constant.PREF_BALANCE, Gson().toJson(balanceList))
        val remainingFreeConversion = app.prefManager.getIntPref(Constant.PREF_FREE_CONVERT_COUNT)
        if (remainingFreeConversion > 0) {
            app.prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, remainingFreeConversion - 1)
        }
        return balanceList
    }
}