package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Url(
    val thumb: String,
    val original: String,
) : Parcelable {

    companion object {
        fun List<Url>.cloned() = ArrayList(map { it.copy() })
    }
}