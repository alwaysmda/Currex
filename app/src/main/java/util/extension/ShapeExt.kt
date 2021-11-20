package util.extension

import android.graphics.drawable.GradientDrawable
import com.xodus.templatefive.R

fun getShape(
    orientation: GradientDrawable.Orientation,
    firstColor: Int,
    secondColor: Int,
    borderColor: Int,
    borderWidth: Int,
    topLeftRadiusX: Int,
    topLeftRadiusY: Int,
    topRightRadiusX: Int,
    topRightRadiusY: Int,
    downRightRadiusX: Int,
    downRightRadiusY: Int,
    downLeftRadiusX: Int,
    downLeftRadiusY: Int
): GradientDrawable {
    val shape = GradientDrawable(orientation, intArrayOf(firstColor, secondColor))
    shape.shape = GradientDrawable.RECTANGLE
    shape.cornerRadii = floatArrayOf(
        convertDPtoPX(topLeftRadiusX.toFloat()),
        convertDPtoPX(topLeftRadiusY.toFloat()),
        convertDPtoPX(topRightRadiusX.toFloat()),
        convertDPtoPX(topRightRadiusY.toFloat()),
        convertDPtoPX(downRightRadiusX.toFloat()),
        convertDPtoPX(downRightRadiusY.toFloat()),
        convertDPtoPX(downLeftRadiusX.toFloat()),
        convertDPtoPX(downLeftRadiusY.toFloat())
    )
    shape.setStroke(borderWidth, borderColor)
    return shape
}

fun getShape(
    orientation: GradientDrawable.Orientation,
    firstColor: Int,
    secondColor: Int,
    borderColor: Int,
    borderWidth: Int,
    topLeftRadius: Int,
    topRightRadius: Int,
    downRightRadius: Int,
    downLeftRadius: Int
): GradientDrawable {
    return getShape(
        orientation,
        firstColor,
        secondColor,
        borderColor,
        borderWidth,
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        downRightRadius,
        downRightRadius,
        downLeftRadius,
        downLeftRadius
    )
}

fun getShape(
    orientation: GradientDrawable.Orientation,
    firstColor: Int,
    secondColor: Int,
    borderColor: Int,
    borderWidth: Int,
    radius: Int
): GradientDrawable {
    return getShape(orientation, firstColor, secondColor, borderColor, borderWidth, radius, radius, radius, radius)
}

fun getShape(
    orientation: GradientDrawable.Orientation,
    firstColor: Int,
    secondColor: Int,
    radius: Int
): GradientDrawable {
    return getShape(
        orientation,
        firstColor,
        secondColor,
        R.color.md_transparent_1000,
        0,
        radius,
        radius,
        radius,
        radius
    )
}

fun getShape(
    backgroundColor: Int,
    borderColor: Int,
    borderWidth: Int,
    radius: Int
): GradientDrawable {
    return getShape(
        GradientDrawable.Orientation.RIGHT_LEFT,
        backgroundColor,
        backgroundColor,
        borderColor,
        borderWidth,
        radius,
        radius,
        radius,
        radius
    )
}

fun getShape(
    backgroundColor: Int,
    topLeftRadius: Int,
    topRightRadius: Int = topLeftRadius,
    downRightRadius: Int = topLeftRadius,
    downLeftRadius: Int = topLeftRadius
): GradientDrawable {
    return getShape(
        GradientDrawable.Orientation.RIGHT_LEFT,
        backgroundColor,
        backgroundColor,
        R.color.md_transparent_1000,
        0,
        topLeftRadius,
        topRightRadius,
        downRightRadius,
        downLeftRadius
    )
}