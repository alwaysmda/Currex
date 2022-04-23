package util

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class StringResource {
    data class Raw(val string: String) : StringResource()
    class Translatable(@StringRes val resId: Int, vararg val args: Any) : StringResource()
    class TranslatablePlural(@PluralsRes val resId: Int, val quantity: Int, vararg val args: Any) : StringResource()

    fun asString(context: Context): String = if (isNotBlank()) {
        when (this) {
            is Raw                -> string
            is Translatable       -> context.getString(resId, *args)
            is TranslatablePlural -> context.resources.getQuantityString(resId, quantity, *args)
        }
    } else {
        ""
    }

    fun isNotBlank(): Boolean {
        return if (this is Raw && string.isNotBlank()) {
            true
        } else if (this is Translatable && resId != 0) {
            true
        } else this is TranslatablePlural && resId != 0
    }
}