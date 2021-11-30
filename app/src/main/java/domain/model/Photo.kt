package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val color: String,
    val url: Url,
) : Parcelable {

    companion object {
        fun List<Photo>.cloned() = ArrayList(map { it.copy() })
    }
}