package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rate(
    val name: String,
    val value: Double,
) : Parcelable {
    companion object {
        fun List<Rate>.cloned() = ArrayList(map { it.copy() })
    }
}