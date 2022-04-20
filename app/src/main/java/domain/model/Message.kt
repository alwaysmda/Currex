package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val user: String,
    val message: String,
) : Parcelable {
    companion object {
        fun List<Message>.cloned() = ArrayList(map { it.copy() })
    }
}