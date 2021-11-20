package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val albumId: Int,
    val id: Int,
    val title: String,
    val url: Url,
) : Parcelable {

    companion object {
        fun List<Photo>.cloned(): List<Photo> {
            val list = arrayListOf<Photo>()
            forEach {
                list.add(Photo(it.albumId, it.id, it.title, it.url))
            }
            return list
        }
    }
}