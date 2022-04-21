package domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import util.extension.separateNumberBy3

@Parcelize
data class Rate(
    val name: String,
    val value: Double,
    var selected: Boolean = false,
) : Parcelable {

    val valueString: String
        get() = value.separateNumberBy3()

    companion object {
        fun List<Rate>.cloned() = ArrayList(map { it.copy() })
    }
}