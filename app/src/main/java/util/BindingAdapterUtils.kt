package util

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun isVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}

@BindingAdapter("text")
fun text(textView: TextView, stringResource: StringResource) {
    textView.text = stringResource.asString(textView.context)
}