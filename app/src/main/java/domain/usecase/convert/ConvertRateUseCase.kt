package domain.usecase.convert

import main.ApplicationClass
import util.Constant
import util.extension.extractNumbers
import util.extension.separateNumberBy3
import javax.inject.Inject

class ConvertRateUseCase @Inject constructor(private val app: ApplicationClass) {
    operator fun invoke(input: String, inputRate: Double, targetRate: Double): String {
        return if (input.isBlank()) {
            ""
        } else {
            val inputDouble = input.extractNumbers().toDouble()
            val result = inputDouble * targetRate / inputRate

            val inputFee = calculateFee(app, inputDouble)
            val targetFee = inputFee * targetRate / inputRate

            result.separateNumberBy3()
        }
    }

    private fun calculateFee(app: ApplicationClass, input: Double): Double {
        val remainingFreeConversion = app.prefManager.getIntPref(Constant.PREF_FREE_CONVERT_COUNT)
        return if (remainingFreeConversion > 0) {
            app.prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, remainingFreeConversion - 1)
            0.0
        } else {
            input * 0.007
        }
    }
}