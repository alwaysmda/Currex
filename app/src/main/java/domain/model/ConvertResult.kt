package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import util.StringResource
import util.extension.separateNumberBy3

@Parcelize
data class ConvertResult(
    val sellValue: Double,
    val receiveValue: Double,
    val sellFee: Double,
    val receiveFee: Double,
    val isValid: Boolean,
    val error: @RawValue StringResource,
    val feeDesc: @RawValue StringResource,
) : Parcelable {

    val sellString: String
        get() = if (sellValue == 0.0) "" else sellValue.separateNumberBy3()
    val receiveString: String
        get() = if (receiveValue == 0.0) "" else receiveValue.separateNumberBy3()
    val sellFeeString: String
        get() = sellFee.separateNumberBy3()

    companion object {
        fun List<ConvertResult>.cloned() = ArrayList(map { it.copy() })
    }
}