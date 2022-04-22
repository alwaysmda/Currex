package domain.usecase.convert

import com.example.currex.R
import domain.model.ConvertResult
import domain.model.Rate
import main.ApplicationClass
import util.Constant
import util.extension.extractNumbers
import util.extension.separateNumberBy3
import javax.inject.Inject

class ConvertRateUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(input: String, inputRate: Rate, targetRate: Rate, isSelling: Boolean, balanceList: ArrayList<Rate>): ConvertResult {
        val inputDouble = if (input.isBlank()) 0.0 else input.extractNumbers().toDouble()
        val result = inputDouble * targetRate.value / inputRate.value

        val sellValue = if (isSelling) inputDouble else result
        val receiveValue = if (isSelling) result else inputDouble
        val sellValueInEur = sellValue / if (isSelling) inputRate.value else targetRate.value

        var sellFee = 0.0
        var receiveFee = 0.0
        val status = getCommissionStatus(sellValueInEur)
        var feeText = status.second
        if (status.first.not() && sellValue != 0.0) {
            sellFee = if (app.appSetting.reduceFeeFromSource) sellValue * app.appSetting.conversionFee else 0.0
            receiveFee = if (app.appSetting.reduceFeeFromTarget) sellFee * targetRate.value / inputRate.value else 0.0
            feeText = if (sellFee == 0.0 && receiveFee == 0.0) {
                app.getString(R.string.conversion_is_free)
            } else {
                app.getString(R.string.commission_fee_values, if (sellFee == 0.0) "free" else sellFee.separateNumberBy3(), if (receiveFee == 0.0) "free" else receiveFee.separateNumberBy3())
            }
        }

        val sellBalance = balanceList[0]
        return when {
            sellValue == 0.0                        -> ConvertResult(sellValue, receiveValue, sellFee, receiveFee, false, "", feeText)
            sellValue > sellBalance.value           -> ConvertResult(sellValue, receiveValue, sellFee, receiveFee, false, app.getString(R.string.insufficient_balance), feeText)
            sellValue + sellFee > sellBalance.value -> ConvertResult(sellValue, receiveValue, sellFee, receiveFee, false, app.getString(R.string.insufficient_balance_for_fee), feeText)
            else                                    -> ConvertResult(sellValue, receiveValue, sellFee, receiveFee, true, "", feeText)
        }
    }

    private fun getCommissionStatus(sellValueInEur: Double): Pair<Boolean, String> {
        var isFree = false
        var status = ""
        when {
            app.appSetting.freeConvert                           -> {
                isFree = true
                status = app.getString(R.string.conversion_is_free)
            }
            app.appSetting.conversionFee == 0.0                  -> {
                isFree = true
                status = app.getString(R.string.commission_fee_is_zero)
            }
            app.appSetting.freeConvertCount > 0                  -> {
                isFree = true
                status = app.resources.getQuantityString(R.plurals.x_free_conversion_left, app.appSetting.freeConvertCount, app.appSetting.freeConvertCount)
            }
            sellValueInEur < app.appSetting.freeConvertBelowXEur -> {
                isFree = true
                status = app.getString(R.string.commission_fee_for_below_x_eur_is_free, app.appSetting.freeConvertBelowXEur.toInt())
            }
            app.appSetting.freeConvertEveryX != 0                -> {
                val convertCount = app.prefManager.getIntPref(Constant.PREF_CONVERT_COUNT)
                isFree = convertCount % app.appSetting.freeConvertEveryX == 0
                if (isFree) {
                    status = app.getString(R.string.every_x_conversion_is_free, app.appSetting.freeConvertEveryX, convertCount + 1)
                }

            }
        }
        return isFree to status
    }
}