package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConvertResult(
    val inputValue: String,
    val targetValue: String,
    val fee: String,
) : Parcelable {

    companion object {
        fun List<ConvertResult>.cloned() = ArrayList(map { it.copy() })
    }
}