package domain.usecase.convert

import com.example.currex.R
import domain.model.ConvertResult
import domain.model.Rate
import main.ApplicationClass
import util.Constant
import util.extension.extractNumbers
import javax.inject.Inject

class ConvertRateUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(input: String, inputRate: Double, targetRate: Double, isSelling: Boolean, balanceList: ArrayList<Rate>): ConvertResult {
        val remainingFreeConvertCount = app.appSetting.freeConvertCount
        var feeText = ""
        if (app.appSetting.freeConvertCount > 0) {
            feeText = app.resources.getQuantityString(R.plurals.x_free_conversion_left, remainingFreeConvertCount, remainingFreeConvertCount)
        }
        return if (input.isBlank()) {
            ConvertResult(0.0, 0.0, 0.0, 0.0, false, "", feeText)
        } else {
            val inputDouble = input.extractNumbers().toDouble()
            val result = inputDouble * targetRate / inputRate


            val sellValue = if (isSelling) inputDouble else result
            var sellFee = 0.0
            var receiveFee = 0.0

            var isFree = false

            when {
                app.appSetting.conversionFee == 0.0   -> isFree = true
                remainingFreeConvertCount <= 0        -> isFree = true
                app.appSetting.freeConvertEveryX != 0 -> {
                    val convertCount = app.prefManager.getIntPref(Constant.PREF_CONVERT_COUNT)
                    isFree = convertCount % app.appSetting.freeConvertEveryX == 0
                }
            }


            if (isFree.not()) {
                sellFee = if (app.appSetting.reduceFeeFromSource) sellValue * app.appSetting.conversionFee else 0.0
                receiveFee = if (app.appSetting.reduceFeeFromTarget) sellFee * targetRate / inputRate else 0.0
//                feeText = app.getString(R.string.convert_fee_desc, lastConvertResult.sellFeeString, sellRate.name)
            }


            val sellBalance = balanceList[0]
            when {
                inputDouble == 0.0                        -> ConvertResult(if (isSelling) inputDouble else result, if (isSelling) result else inputDouble, sellFee, receiveFee, false, "", feeText)
                inputDouble > sellBalance.value           -> ConvertResult(if (isSelling) inputDouble else result, if (isSelling) result else inputDouble, sellFee, receiveFee, false, app.getString(R.string.insufficient_balance), feeText)
                inputDouble + sellFee > sellBalance.value -> ConvertResult(if (isSelling) inputDouble else result, if (isSelling) result else inputDouble, sellFee, receiveFee, false, app.getString(R.string.insufficient_balance_for_fee), feeText)
                else                                      -> ConvertResult(if (isSelling) inputDouble else result, if (isSelling) result else inputDouble, sellFee, receiveFee, true, "", feeText)
            }
        }
    }
}