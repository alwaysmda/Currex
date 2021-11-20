package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Url(
    val thumb: String,
    val original: String,
) : Parcelable {

    companion object {
        fun List<Url>.cloned(): List<Url> {
            val list = arrayListOf<Url>()
            forEach {
                list.add(Url(it.thumb, it.original))
            }
            return list
        }
    }
}