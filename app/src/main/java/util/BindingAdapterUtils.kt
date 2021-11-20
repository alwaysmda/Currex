package util

import android.graphics.Typeface
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: String?) {
    if (url.isNullOrEmpty().not()) {
        Picasso.get().load(url)
            //            .placeholder(R.color.md_grey_300).error(R.color.md_grey_500).fit().centerInside()
            .into(imageView)
    }
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, drawable: Int?) {
    drawable?.let {
        Picasso.get().load(it)
            //            .placeholder(R.color.md_grey_300).error(R.color.md_grey_500).fit().centerInside()
            .into(imageView)
    }
}

//@BindingAdapter("imageUrl")
//fun imageUrl(imageView: ImageView, drawable: Int?) {
//    Glide
//        .with(imageView)
//        .load(drawable)
//        .centerInside()
//        .placeholder(R.color.md_grey_300)
//        .error(R.color.md_grey_500)
//        .into(imageView)
//}

@BindingAdapter("fonti")
fun fonti(textView: TextView, font: Typeface?) {
    font?.let {
        textView.typeface = it
    }
}

@BindingAdapter("fonte")
fun fonte(editText: EditText, font: Typeface?) {
    font?.let {
        editText.typeface = it
    }
}

@BindingAdapter("fonte")
fun fonte(textInputLayout: TextInputLayout, font: Typeface?) {
    font?.let {
        textInputLayout.typeface = it
    }
}

@BindingAdapter("isVisible")
fun isVisible(view: View, visible: Boolean) {
    view.isVisible = visible
}