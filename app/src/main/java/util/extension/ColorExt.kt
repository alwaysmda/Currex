package util.extension

import android.graphics.Color
import androidx.core.content.ContextCompat
import main.ApplicationClass
import java.util.*


/**
 * @param factor must be less than 1. less is darker.
 */
fun darkenColor(color: Int, factor: Float): Int {
    val a = Color.alpha(color)
    val r = Math.round(Color.red(color) * factor)
    val g = Math.round(Color.green(color) * factor)
    val b = Math.round(Color.blue(color) * factor)
    return Color.argb(
        a,
        Math.min(r, 255),
        Math.min(g, 255),
        Math.min(b, 255)
    )
}


fun getRandomColor(contrast: Int = 500): Int {
    val appClass = ApplicationClass.getInstance()
    val colors: ArrayList<Int> = arrayListOf(
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_red_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_pink_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_purple_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_deep_purple_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_indigo_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_blue_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_light_blue_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_cyan_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_teal_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_green_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_light_green_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_lime_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_yellow_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_amber_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_orange_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_deep_orange_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_brown_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_grey_$contrast", "color", appClass.packageName)),
        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_blue_grey_$contrast", "color", appClass.packageName))
    )
    return colors[Random().nextInt(colors.size - 1)]
}


fun isColorDark(color: Int): Boolean {
    val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
    return darkness >= 0.5
}
