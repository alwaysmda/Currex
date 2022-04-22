package domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import util.extension.separateNumberBy3

@Entity(tableName = "tbl_balance")
@Parcelize
data class Rate(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    var value: Double,
    var selected: Boolean = false,
    var isSell: Boolean = false,
    var isReceive: Boolean = false,
) : Parcelable {

    val valueString: String
        get() = value.separateNumberBy3()

    companion object {
        fun List<Rate>.cloned() = ArrayList(map { it.copy() })
    }
}