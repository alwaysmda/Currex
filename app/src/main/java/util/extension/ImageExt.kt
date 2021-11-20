package util.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import main.ApplicationClass
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


//fun convertImageToBase64(imageResource: Int): String {
//    val bm = BitmapFactory.decodeResource(ApplicationClass.getInstance().resources, imageResource)
//    val baos = ByteArrayOutputStream()
//    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
//    val b = baos.toByteArray()
//    return Base64.encodeToString(b, Base64.DEFAULT)
//}
//
//fun convertBase64ToBitmapDrawable(encodedBase64String: String): BitmapDrawable {
//    val decodedString = Base64.decode(encodedBase64String, Base64.DEFAULT)
//    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//    return BitmapDrawable(ApplicationClass.getInstance().resources, decodedByte)
//}

fun reduceBitmapQuality(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): Bitmap {
    val bytearrayoutputstream = ByteArrayOutputStream()
    bitmap.compress(format, quality, bytearrayoutputstream)
    val BYTE = bytearrayoutputstream.toByteArray()
    return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.size)
}

fun reduceBitmapSize(_bitmap: Bitmap, format: Bitmap.CompressFormat, maxSize: Int): Bitmap {
    var bitmap = _bitmap
    val bytearrayoutputstream = ByteArrayOutputStream()
    bitmap.compress(format, 90, bytearrayoutputstream)
    val BYTE = bytearrayoutputstream.toByteArray()
    bitmap = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.size)
    return if (bitmap.byteCount > maxSize) {
        reduceBitmapSize(bitmap, format, maxSize)
    } else {
        bitmap
    }
}

fun reduceBitmapResolution(image: Bitmap, maxSize: Int): Bitmap {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun resizeImage(image: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(image, width, height, true)
}

fun pathToBitmap(path: String): Bitmap? {
    val imgFile = File(path)
    return if (imgFile.exists()) {
        BitmapFactory.decodeFile(imgFile.absolutePath)
    } else {
        null
    }
}

fun convertBitmapToDrawable(bitmap: Bitmap): Drawable {
    return BitmapDrawable(ApplicationClass.getInstance().resources, bitmap)
}

fun convertDrawableToBitmap(resourceId: Int): Bitmap {
    return BitmapFactory.decodeResource(ApplicationClass.getInstance().resources, resourceId)
}


fun convertDrawableToBitmap(drawable: Drawable): Bitmap {

    if (drawable is BitmapDrawable) {
        drawable.bitmap?.let {
            return it
        }
    }
    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    }

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}


fun convertBitmapToFile(bitmap: Bitmap, path: String, fileName: String): File? {
    val filePath = File(path)
    // Make sure the path directory exists.
    if (!filePath.exists()) {
        // Make it, if it doesn't exit
        log("Create Directory=" + filePath.mkdirs())
    }
    val file = File(path, fileName)
    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
    val bitmapdata = bos.toByteArray()

    //write the bytes in file
    return try {
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

}

fun convertFileToBitmap(file: File): Bitmap {
    return BitmapFactory.decodeFile(file.path)
}

fun convertViewToBitmap(v: View, width: Int, height: Int): Bitmap {
    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    v.layout(0, 0, width, height)
    v.draw(c)
    return b
}

fun getDominantColor(bitmap: Bitmap): Int {
    val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
    val color = newBitmap.getPixel(0, 0)
    newBitmap.recycle()
    return color
}

fun getDominantColor(resourceId: Int): Int {
    return getDominantColor(Bitmap.createScaledBitmap(convertDrawableToBitmap(resourceId), 1, 1, true))
}