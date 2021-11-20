package util.extension

import android.animation.ValueAnimator
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import main.ApplicationClass

fun getColor(view: ImageView, x: Int, y: Int): Int {
    return (view.drawable as BitmapDrawable).bitmap.getPixel(x, y)
}

fun setTextViewGradient(textView: TextView, firstColor: Int, secondColor: Int, center: Boolean) {
    val shader: Shader
    shader = if (center) {
        RadialGradient(
            (textView.width / 2).toFloat(), (textView.height / 2).toFloat(), (textView.width / 2).toFloat(),
            firstColor, secondColor, Shader.TileMode.MIRROR
        )
    } else {
        RadialGradient(
            textView.x, textView.y, textView.width.toFloat(),
            firstColor, secondColor, Shader.TileMode.MIRROR
        )
    }
    textView.paint.shader = shader
}

fun setEditTextCursorColor(view: EditText, @ColorInt color: Int) {
    try {
        // Get the cursor resource id
        var field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        field.isAccessible = true
        val drawableResId = field.getInt(view)

        // Get the editor
        field = TextView::class.java.getDeclaredField("mEditor")
        field.isAccessible = true
        val editor = field.get(view)

        // Get the drawable and set a color filter
        val drawable: Drawable? = ContextCompat.getDrawable(view.context, drawableResId)
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        val drawables = drawable?.let { arrayOf(it, it) }

        // Set the drawables
        field = editor.javaClass.getDeclaredField("mCursorDrawable")
        field.isAccessible = true
        field.set(editor, drawables)
    } catch (ignored: Exception) {
    }

}


fun setEditTextCursor(view: EditText, @DrawableRes drawable: Int) {
    try {
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(view)

        val fSelectHandleLeft = editor.javaClass.getDeclaredField("mSelectHandleLeft")
        val fSelectHandleRight = editor.javaClass.getDeclaredField("mSelectHandleRight")
        val fSelectHandleCenter = editor.javaClass.getDeclaredField("mSelectHandleCenter")

        fSelectHandleLeft.isAccessible = true
        fSelectHandleRight.isAccessible = true
        fSelectHandleCenter.isAccessible = true

        fSelectHandleLeft.set(editor, drawable)
        fSelectHandleRight.set(editor, drawable)
        fSelectHandleCenter.set(editor, drawable)
    } catch (ignored: Exception) {
    }

}

fun animateTextViewText(
    textView: TextView,
    text: String,
    delay: Int = 60,
    startDelay: Long = 0,
    onFinish: () -> Unit = {}
) {
    Handler(Looper.getMainLooper()).postDelayed({
        for (i in 0 until text.length) {
            Handler(Looper.getMainLooper()).postDelayed({ textView.text = text.substring(0, i + 1) }, (i * delay).toLong())
        }
        Handler(Looper.getMainLooper()).postDelayed({ onFinish() }, (text.length * delay).toLong())
    }, startDelay)
}


fun animateEditTextHint(editText: EditText, text: String) {
    animateEditTextHint(editText, text, 60)
}

fun animateEditTextHint(editText: EditText, text: String, delay: Int) {
    for (i in 0 until text.length) {
        Handler(Looper.getMainLooper()).postDelayed({ editText.hint = text.substring(0, i + 1) }, (i * delay).toLong())
    }
}

fun animateViews(views: Array<View?>, show: Boolean, duration: Long = 500, startDelay: Long = 0, delay: Long = 100, onFinish: () -> (Unit?) = {}) {
    var time = 0L
    var alpha = if (show) 1F else 0F
    for (view in views) {
        view?.animate()?.alpha(alpha)?.setStartDelay(startDelay + time)?.setDuration(duration)?.start()
        time += delay
    }
    Handler(Looper.getMainLooper()).postDelayed({ onFinish() }, time + startDelay)
}

fun transition(vararg viewPair: Pair<View, View>) { //todo : FAILED -> Reason : Constraints effect the animation
    for (item in viewPair) {
        transition(item.first, item.second)
    }
}

fun transition(firstView: View, secondView: View, duration: Long = 500) { //todo : FAILED -> Reason : Constraints effect the animation

    var initLeft = 0F
    var initTop = 0F
    var initHeight = 0
    var initWidth = 0

    var deltaLeft = 0F
    var deltaTop = 0F
    var deltaHeight = 0
    var deltaWidth = 0

    var initTextSize = 0F
    var deltaTextSize = 0F

    initLeft = firstView.x
    deltaLeft = secondView.x - firstView.x

    initTop = firstView.y
    deltaTop = secondView.y - firstView.y


    if (firstView is TextView && secondView is TextView) {
        initTextSize = convertPXtoSP(firstView.textSize)
        deltaTextSize = convertPXtoSP(secondView.textSize - firstView.textSize)
    } else {
        initHeight = firstView.height
        deltaHeight = secondView.height - firstView.height

        initWidth = firstView.width
        deltaWidth = secondView.width - firstView.width
    }


    val animator = ValueAnimator.ofFloat(0F, 100F)
    animator.addUpdateListener {
        val percent = it.animatedValue as Float
        firstView.x = ((percent * deltaLeft) / 100F) + initLeft
        firstView.y = (percent * deltaTop) / 100F + initTop
        if (firstView is TextView) {
            firstView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ((percent * deltaTextSize) / 100F) + initTextSize)
        } else {
            val params = firstView.layoutParams
            params.height = ((percent * deltaHeight) / 100F).toInt() + initHeight
            params.width = ((percent * deltaWidth) / 100F).toInt() + initWidth
            firstView.layoutParams = params
        }
    }
    animator.interpolator = AccelerateInterpolator()
    animator.duration = duration
    animator.start()
}

fun ProgressBar.setTint(color: Int) {
    indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun ViewGroup.changeChildFont(typeface: Typeface) {
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        if (view is TextView) {
            view.typeface = ApplicationClass.getInstance().m.fontLight
        }
    }
}

fun ViewGroup.animateChild() {
    val views: Array<View?> = arrayOfNulls(childCount)
    for (i in 0 until childCount) {
        val view = getChildAt(i)
        views[i] = view
    }
    animateViews(views, false, startDelay = 2000)
}

fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.underlineRemove() {
    paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
}

fun TextView.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.strikeThroughRemove() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

fun TextView.bold() {
    paintFlags = paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
}

fun TextView.boldRemove() {
    paintFlags = paintFlags and Paint.FAKE_BOLD_TEXT_FLAG.inv()
}
