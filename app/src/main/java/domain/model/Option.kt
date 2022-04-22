package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Option<T>(
    val name: String,
    var value: @RawValue T,
    var selected: Boolean = false,
) : Parcelable {


    companion object {
        fun <T> List<Option<T>>.cloned() = ArrayList(map { it.copy() })
    }
}