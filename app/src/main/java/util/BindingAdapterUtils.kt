package util

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun isVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}