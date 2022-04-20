package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseExchangeRate(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: ArrayList<Rate>,
) : Parcelable {
    companion object {
        fun List<ResponseExchangeRate>.cloned() = ArrayList(map { it.copy() })
    }
}