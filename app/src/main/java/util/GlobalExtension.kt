package util
//
//import android.animation.ValueAnimator
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.ActivityManager
//import android.app.Application
//import android.content.ClipData
//import android.content.ClipDescription
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageInfo
//import android.content.pm.PackageManager
//import android.content.res.Resources
//import android.database.Cursor
//import android.graphics.*
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.media.MediaScannerConnection
//import android.net.ConnectivityManager
//import android.net.Uri
//import android.os.*
//import android.provider.MediaStore
//import android.provider.Settings
//import android.text.TextUtils
//import android.util.DisplayMetrics
//import android.util.Log
//import android.util.TypedValue
//import android.view.View
//import android.view.ViewGroup
//import android.view.WindowManager
//import android.view.animation.AccelerateInterpolator
//import android.view.inputmethod.InputMethodManager
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import android.widget.Toast
//import androidx.annotation.ColorInt
//import androidx.annotation.DrawableRes
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.MutableLiveData
//import com.google.android.material.snackbar.Snackbar
//import com.xodus.templatefive.BuildConfig
//import com.xodus.templatefive.R
//import main.ApplicationClass
//import org.json.JSONArray
//import org.json.JSONException
//import org.json.JSONObject
//import java.io.*
//import java.security.KeyFactory
//import java.security.MessageDigest
//import java.security.PrivateKey
//import java.security.spec.PKCS8EncodedKeySpec
//import java.text.DecimalFormat
//import java.text.SimpleDateFormat
//import java.util.*
//import java.util.zip.ZipEntry
//import java.util.zip.ZipInputStream
//
//fun getLocation(): String {
//    val CALL_STACK_INDEX = 2
//    // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
//    // because Robolectric runs them on the JVM but on Android the elements are different.
//    val stackTrace = Throwable().stackTrace
//    if (stackTrace.size <= CALL_STACK_INDEX) {
//        throw IllegalStateException(
//            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
//        )
//    }
//
//    var element: StackTraceElement = stackTrace[2]
//    for (item in stackTrace) {
//        if (item.methodName != "getLocation" && item.methodName != "log") {
//            element = item
//            break
//        }
//    }
//
//    return ".(" + element.toString().substring(
//        element.toString().lastIndexOf(
//            "("
//        ) + 1, element.toString().lastIndexOf(")")
//    ) + ")"
//}
//
//fun getLocation(index: Int): String {
//    val stackTrace = Throwable().stackTrace
//    if (stackTrace.size <= index) {
//        throw IllegalStateException(
//            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
//        )
//    }
//    //        for (int i = 0; i < stackTrace.length; i++) {
//    //            log(LOG, "TRACETRACE INDEX=" + i + "|" + stackTrace[i].toString());
//    //        }
//
//
//    return ".(" + stackTrace[index].toString().substring(
//        stackTrace[index].toString().lastIndexOf("(") + 1,
//        stackTrace[index].toString().lastIndexOf(")")
//    ) + ")"
//}
//
//fun getStackTrace(): String {
//    val stackTrace = Throwable().stackTrace
//    var stack = ""
//    for (i in stackTrace.indices) {
//        stack += "\n.(" + stackTrace[i].toString().substring(
//            stackTrace[i].toString().lastIndexOf("(") + 1,
//            stackTrace[i].toString().lastIndexOf(")")
//        ) + ")"
//    }
//    return stack
//}
//
//fun getRandomString(length: Int): String {
//    val AB = "0123456789abcdefghijklmnopqrstuvwxyz"
//    val rnd = Random()
//    val sb = StringBuilder(length)
//    for (i in 0 until length)
//        sb.append(AB[rnd.nextInt(AB.length)])
//    return sb.toString()
//}
//
//fun getRandomInt(length: Int): Int {
//    val AB = "0123456789"
//    val rnd = Random()
//    val sb = StringBuilder(length)
//    for (i in 0 until length)
//        sb.append(AB[rnd.nextInt(AB.length)])
//    return Integer.valueOf(sb.toString())
//}
//
//fun translate(_c: Any): String {
//    var c = _c.toString()
//    val enN = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
//    val faN = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
//
//    if (ApplicationClass.getInstance().languageManager.currentLanguage.value == Constant.Languages.FA.value) {
//        for (i in 0..9) {
//            c = c.replace(enN[i], faN[i])
//        }
//    } else {
//        for (i in 0..9) {
//            c = c.replace(faN[i], enN[i])
//        }
//    }
//    return c
//}
//
//fun translateToPersian(_c: Any): String {
//    var c = _c.toString()
//    val enN = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
//    val faN = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
//
//    if (ApplicationClass.getInstance().prefManager.getStringPref(Constant.PREF_LANGUAGE).equals("fa")) {
//        for (i in 0..9) {
//            c = c.replace(enN[i], faN[i])
//        }
//    } else {
//        for (i in 0..9) {
//            c = c.replace(faN[i], enN[i])
//        }
//    }
//    return c
//}
//
//fun translateToEnglish(_c: Any): String {
//    var c = _c.toString()
//    val faN = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
//    val enN = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
//    for (i in 0..9) {
//        c = c.replace(faN[i], enN[i])
//    }
//    return c
//}
//
//
//fun getShape(
//    orientation: GradientDrawable.Orientation,
//    firstColor: Int,
//    secondColor: Int,
//    borderColor: Int,
//    borderWidth: Int,
//    topLeftRadiusX: Int,
//    topLeftRadiusY: Int,
//    topRightRadiusX: Int,
//    topRightRadiusY: Int,
//    downRightRadiusX: Int,
//    downRightRadiusY: Int,
//    downLeftRadiusX: Int,
//    downLeftRadiusY: Int
//): GradientDrawable {
//    val shape = GradientDrawable(orientation, intArrayOf(firstColor, secondColor))
//    shape.shape = GradientDrawable.RECTANGLE
//    shape.cornerRadii = floatArrayOf(
//        convertDPtoPX(topLeftRadiusX.toFloat()),
//        convertDPtoPX(topLeftRadiusY.toFloat()),
//        convertDPtoPX(topRightRadiusX.toFloat()),
//        convertDPtoPX(topRightRadiusY.toFloat()),
//        convertDPtoPX(downRightRadiusX.toFloat()),
//        convertDPtoPX(downRightRadiusY.toFloat()),
//        convertDPtoPX(downLeftRadiusX.toFloat()),
//        convertDPtoPX(downLeftRadiusY.toFloat())
//    )
//    shape.setStroke(borderWidth, borderColor)
//    return shape
//}
//
//fun getShape(
//    orientation: GradientDrawable.Orientation,
//    firstColor: Int,
//    secondColor: Int,
//    borderColor: Int,
//    borderWidth: Int,
//    topLeftRadius: Int,
//    topRightRadius: Int,
//    downRightRadius: Int,
//    downLeftRadius: Int
//): GradientDrawable {
//    return getShape(
//        orientation,
//        firstColor,
//        secondColor,
//        borderColor,
//        borderWidth,
//        topLeftRadius,
//        topLeftRadius,
//        topRightRadius,
//        topRightRadius,
//        downRightRadius,
//        downRightRadius,
//        downLeftRadius,
//        downLeftRadius
//    )
//}
//
//fun getShape(
//    orientation: GradientDrawable.Orientation,
//    firstColor: Int,
//    secondColor: Int,
//    borderColor: Int,
//    borderWidth: Int,
//    radius: Int
//): GradientDrawable {
//    return getShape(orientation, firstColor, secondColor, borderColor, borderWidth, radius, radius, radius, radius)
//}
//
//fun getShape(
//    orientation: GradientDrawable.Orientation,
//    firstColor: Int,
//    secondColor: Int,
//    radius: Int
//): GradientDrawable {
//    return getShape(
//        orientation,
//        firstColor,
//        secondColor,
//        R.color.md_transparent_1000,
//        0,
//        radius,
//        radius,
//        radius,
//        radius
//    )
//}
//
//fun getShape(
//    backgroundColor: Int,
//    borderColor: Int,
//    borderWidth: Int,
//    radius: Int
//): GradientDrawable {
//    return getShape(
//        GradientDrawable.Orientation.RIGHT_LEFT,
//        backgroundColor,
//        backgroundColor,
//        borderColor,
//        borderWidth,
//        radius,
//        radius,
//        radius,
//        radius
//    )
//}
//
//fun getShape(
//    backgroundColor: Int,
//    topLeftRadius: Int,
//    topRightRadius: Int = topLeftRadius,
//    downRightRadius: Int = topLeftRadius,
//    downLeftRadius: Int = topLeftRadius
//): GradientDrawable {
//    return getShape(
//        GradientDrawable.Orientation.RIGHT_LEFT,
//        backgroundColor,
//        backgroundColor,
//        R.color.md_transparent_1000,
//        0,
//        topLeftRadius,
//        topRightRadius,
//        downRightRadius,
//        downLeftRadius
//    )
//}
//
////fun convertImageToBase64(imageResource: Int): String {
////    val bm = BitmapFactory.decodeResource(ApplicationClass.getInstance().resources, imageResource)
////    val baos = ByteArrayOutputStream()
////    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
////    val b = baos.toByteArray()
////    return Base64.encodeToString(b, Base64.DEFAULT)
////}
////
////fun convertBase64ToBitmapDrawable(encodedBase64String: String): BitmapDrawable {
////    val decodedString = Base64.decode(encodedBase64String, Base64.DEFAULT)
////    val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
////    return BitmapDrawable(ApplicationClass.getInstance().resources, decodedByte)
////}
//
//fun reduceBitmapQuality(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): Bitmap {
//    val bytearrayoutputstream = ByteArrayOutputStream()
//    bitmap.compress(format, quality, bytearrayoutputstream)
//    val BYTE = bytearrayoutputstream.toByteArray()
//    return BitmapFactory.decodeByteArray(BYTE, 0, BYTE.size)
//}
//
//fun reduceBitmapSize(_bitmap: Bitmap, format: Bitmap.CompressFormat, maxSize: Int): Bitmap {
//    var bitmap = _bitmap
//    val bytearrayoutputstream = ByteArrayOutputStream()
//    bitmap.compress(format, 90, bytearrayoutputstream)
//    val BYTE = bytearrayoutputstream.toByteArray()
//    bitmap = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.size)
//    return if (bitmap.byteCount > maxSize) {
//        reduceBitmapSize(bitmap, format, maxSize)
//    } else {
//        bitmap
//    }
//}
//
//fun reduceBitmapResolution(image: Bitmap, maxSize: Int): Bitmap {
//    var width = image.width
//    var height = image.height
//    val bitmapRatio = width.toFloat() / height.toFloat()
//    if (bitmapRatio > 1) {
//        width = maxSize
//        height = (width / bitmapRatio).toInt()
//    } else {
//        height = maxSize
//        width = (height * bitmapRatio).toInt()
//    }
//    return Bitmap.createScaledBitmap(image, width, height, true)
//}
//
//fun resizeImage(image: Bitmap, width: Int, height: Int): Bitmap {
//    return Bitmap.createScaledBitmap(image, width, height, true)
//}
//
//fun pathToBitmap(path: String): Bitmap? {
//    val imgFile = File(path)
//    return if (imgFile.exists()) {
//        BitmapFactory.decodeFile(imgFile.absolutePath)
//    } else {
//        null
//    }
//}
//
//fun convertBitmapToDrawable(bitmap: Bitmap): Drawable {
//    return BitmapDrawable(ApplicationClass.getInstance().resources, bitmap)
//}
//
//fun convertDrawableToBitmap(resourceId: Int): Bitmap {
//    return BitmapFactory.decodeResource(ApplicationClass.getInstance().resources, resourceId)
//}
//
//
//fun convertDrawableToBitmap(drawable: Drawable): Bitmap {
//
//    if (drawable is BitmapDrawable) {
//        drawable.bitmap?.let {
//            return it
//        }
//    }
//    val bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
//        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
//    } else {
//        Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//    }
//
//    val canvas = Canvas(bitmap)
//    drawable.setBounds(0, 0, canvas.width, canvas.height)
//    drawable.draw(canvas)
//    return bitmap
//}
//
//
//fun convertBitmapToFile(bitmap: Bitmap, path: String, fileName: String): File? {
//    val filePath = File(path)
//    // Make sure the path directory exists.
//    if (!filePath.exists()) {
//        // Make it, if it doesn't exit
//        log("Create Directory=" + filePath.mkdirs())
//    }
//    val file = File(path, fileName)
//    //Convert bitmap to byte array
//    val bos = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
//    val bitmapdata = bos.toByteArray()
//
//    //write the bytes in file
//    return try {
//        val fos = FileOutputStream(file)
//        fos.write(bitmapdata)
//        fos.flush()
//        fos.close()
//        file
//    } catch (e: Exception) {
//        e.printStackTrace()
//        null
//    }
//
//}
//
//fun convertFileToBitmap(file: File): Bitmap {
//    return BitmapFactory.decodeFile(file.path)
//}
//
//@SuppressLint("HardwareIds")
//fun getAndroidID(): String {
//    return Settings.Secure.getString(ApplicationClass.getInstance().contentResolver, Settings.Secure.ANDROID_ID)
//}
//
//
//fun getUUID(name: String? = null): String {
//    return UUID.fromString(name).toString()
//}
//
//
//fun getPackageInfo(): PackageInfo {
//    return ApplicationClass.getInstance().packageManager.getPackageInfo(ApplicationClass.getInstance().packageName, 0)
//}
//
//fun getMCryptAESKey(password: String): String? {
//    var keyString: String? = null
//    try {
//        var key = password.toByteArray(Charsets.UTF_8)
//        val sha = MessageDigest.getInstance("SHA-1")
//        key = sha.digest(key)
//        key = Arrays.copyOf(key, 16) // use only first 128 bit
//        keyString = String(key, Charsets.UTF_8)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return keyString
//}
//
//fun moveFile(inputPath: String, inputFile: String, outputPath: String): Boolean {
//    val inputStream: InputStream?
//    val outputStream: OutputStream?
//    try {
//        //create output directory if it doesn't exist
//        val dir = File(outputPath)
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }
//        inputStream = FileInputStream(inputPath + inputFile)
//        outputStream = FileOutputStream(outputPath + inputFile)
//        val buffer = ByteArray(1024)
//        var read: Int
//        read = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//        inputStream.close()
//        // write the output file
//        outputStream.flush()
//        outputStream.close()
//        // delete the original file
//        File(inputPath + inputFile).delete()
//    } catch (e: Exception) {
//        log("tag", e.message)
//        return false
//    }
//
//    return true
//}
//
//fun deleteFile(inputPath: String, inputFile: String): Boolean {
//    return try {
//        // delete the original file
//        File(inputPath + inputFile).delete()
//    } catch (e: Exception) {
//        log("tag", e.message)
//        false
//    }
//
//}
//
//fun deleteFolder(path: String) {
//    val file = File(path)
//    if (file.isDirectory)
//        for (child in file.listFiles()!!)
//            deleteFolder(child.path)
//    file.delete()
//}
//
//fun copyFile(inputPath: String, inputFile: String, outputPath: String): Boolean {
//    var inputStream: InputStream?
//    var outputStream: OutputStream?
//    try {
//        //create output directory if it doesn't exist
//        val dir = File(outputPath)
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }
//        inputStream = FileInputStream(inputPath + inputFile)
//        outputStream = FileOutputStream(outputPath + inputFile)
//        val buffer = ByteArray(1024)
//        var read: Int
//        read = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//        inputStream.close()
//        // write the output file (You have now copied the file)
//        outputStream.flush()
//        outputStream.close()
//    } catch (e: FileNotFoundException) {
//        log("tag", e.message)
//        return false
//    } catch (e: Exception) {
//        log("tag", e.message)
//        return false
//    }
//
//    return true
//}
//
//fun copyFile(inputPath: String, outputPath: String): Boolean {
//    var inputStream: InputStream?
//    var outputStream: OutputStream?
//    try {
//        //create output directory if it doesn't exist
//        val dir = File(outputPath.substring(0, outputPath.lastIndexOf("/")))
//        if (!dir.exists()) {
//            dir.mkdirs()
//        }
//        inputStream = FileInputStream(inputPath)
//        outputStream = FileOutputStream(outputPath)
//        val buffer = ByteArray(1024)
//        var read: Int
//        read = inputStream.read(buffer)
//        while (read != -1) {
//            outputStream.write(buffer, 0, read)
//            read = inputStream.read(buffer)
//        }
//        inputStream.close()
//        // write the output file (You have now copied the file)
//        outputStream.flush()
//        outputStream.close()
//    } catch (fnfe1: FileNotFoundException) {
//        log("tag", fnfe1.message)
//        return false
//    } catch (e: Exception) {
//        log("tag", e.message)
//        return false
//    }
//
//    return true
//}
//
//fun renameFile(path: String, fileName: String, newName: String): Boolean {
//    val oldPath = "$path/$fileName"
//    val newPath = "$path/$newName"
//    val file = File(oldPath)
//    val newFile = File(newPath)
//    return file.renameTo(newFile)
//}
//
//fun createFileFromString(data: String?, inputPath: String, fileName: String): Boolean {
//    data?.let {
//        // Get the directory for the user's public pictures directory.
//        val path = File(inputPath)
//        // Make sure the path directory exists.
//        if (!path.exists()) {
//            // Make it, if it doesn't exit
//            path.mkdirs()
//        }
//        val file = File(path, fileName)
//        // Save your stream, don't forget to flush() it before closing it.
//        try {
//            file.createNewFile()
//            val fOut = FileOutputStream(file)
//            val myOutWriter = OutputStreamWriter(fOut)
//            myOutWriter.append(it)
//            myOutWriter.close()
//            fOut.flush()
//            fOut.close()
//        } catch (e: IOException) {
//            log("Exception", "File write failed: $e")
//            return false
//        }
//
//        log("History File Created")
//        return true
//    } ?: run {
//        log("No Data")
//        return false
//    }
//}
//
//fun getScreenWidth(): Int {
//    return Resources.getSystem().displayMetrics.widthPixels
//}
//
//fun getScreenHeight(): Int {
//    return Resources.getSystem().displayMetrics.heightPixels
//}
//
//private fun getSoftButtonsBarHeight(activity: Activity): Int {
//    // getRealMetrics is only available with API 17 and +
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//        val metrics = DisplayMetrics()
//        activity.windowManager.defaultDisplay.getMetrics(metrics)
//        val usableHeight = metrics.heightPixels
//        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
//        val realHeight = metrics.heightPixels
//        return if (realHeight > usableHeight)
//            realHeight - usableHeight
//        else
//            0
//    }
//    return 0
//}
//
//fun getStatusBarHeight(): Int {
//    var result = 0
//    val resourceId = ApplicationClass.getInstance().resources.getIdentifier("status_bar_height", "dimen", "android")
//    if (resourceId > 0) {
//        result = ApplicationClass.getInstance().resources.getDimensionPixelSize(resourceId)
//    }
//    return result
//}
//
//fun convertPXtoSP(px: Float): Float {
//    return px / ApplicationClass.getInstance().resources.displayMetrics.scaledDensity
//}
//
//fun convertDPtoPX(dp: Float): Float {
//    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationClass.getInstance().resources.displayMetrics)
//}
//
//fun convertPXtoDP(px: Float): Float {
//    return px / (ApplicationClass.getInstance().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
//}
//
//fun convertSPtoPX(sp: Float): Int {
//    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ApplicationClass.getInstance().resources.displayMetrics)
//        .toInt()
//}
//
//fun convertDPtoSP(dp: Float): Int {
//    return (convertDPtoPX(dp) / ApplicationClass.getInstance().resources.displayMetrics.scaledDensity).toInt()
//}
//
//fun getScreenDensity(): Float {
//    return ApplicationClass.getInstance().resources.displayMetrics.density
//}
//
//fun getColor(view: ImageView, x: Int, y: Int): Int {
//    return (view.drawable as BitmapDrawable).bitmap.getPixel(x, y)
//}
//
////fun HMACSHA256(key: String, value: String): String? {
////    var result: String? = null
////    try {
////        val keyBytes = key.toByteArray()
////        val signingKey = SecretKeySpec(keyBytes, "HmacSHA256")
////        val mac = Mac.getInstance("HmacSHA256")
////        mac.init(signingKey)
////        val rawHmac = mac.doFinal(value.toByteArray())
////        val hexBytes = Hex().encode(rawHmac)
////        result = String(hexBytes, Charsets.UTF_8)
////    } catch (e: Exception) {
////        log(e.message)
////    }
////    return result
////}
//
//
//fun unzip(inputPath: String, fileName: String, outputPath: String): Boolean {
//    val `is`: InputStream
//    val zis: ZipInputStream
//    try {
//        val directory = File(outputPath)
//        if (directory.exists().not()) {
//            directory.mkdirs()
//        }
//
//        var filename: String
//        `is` = FileInputStream(inputPath + fileName)
//        zis = ZipInputStream(BufferedInputStream(`is`))
//        var ze: ZipEntry?
//        val buffer = ByteArray(1024)
//        var count: Int
//        while (zis.nextEntry.also { ze = it } != null) {
//            filename = ze!!.name
//
//            if (filename.contains("MACOSX").not()) {
//                // Need to create directories if not exists, or
//                // it will generate an Exception...
//                if (ze!!.isDirectory) {
//                    val fmd = File(outputPath.toString() + filename)
//                    fmd.mkdirs()
//                    continue
//                }
//
//
//                val fout = FileOutputStream(outputPath.toString() + filename)
//                while (zis.read(buffer).also { count = it } != -1) {
//                    fout.write(buffer, 0, count)
//                }
//                fout.close()
//                zis.closeEntry()
//            }
//        }
//        zis.close()
//    } catch (e: IOException) {
//        e.printStackTrace()
//        return false
//    }
//
//    return true
//}
//
//fun convertStreamToString(inputStream: InputStream): String {
//    var sb = StringBuilder()
//    try {
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        sb = StringBuilder()
//        var line: String
//        line = reader.readLine()
//        while (line != null) {
//            sb.append(line)
//            line = reader.readLine()
//        }
//        reader.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return sb.toString()
//}
//
//fun getStringFromFile(file: File): String? {
//    val ret: String
//    try {
//        if (file.exists()) {
//            val fin = FileInputStream(file)
//            ret = convertStreamToString(fin)
//            //Make sure you close all streams.
//            fin.close()
//        } else {
//            return null
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return null
//    }
//
//    return ret
//}
//
//fun getStringFromFile(filePath: String): String? {
//    return getStringFromFile(File(filePath))
//}
//
//
//fun getPrivateKeyFromRSA(filepath: String): PrivateKey? {
//    var key: PrivateKey? = null
//    try {
//        val file = File(filepath)
//        val size = file.length().toInt()
//        val bytes = ByteArray(size)
//        val spec = PKCS8EncodedKeySpec(bytes)
//        val kf = KeyFactory.getInstance("RSA")
//        key = kf.generatePrivate(spec)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return key
//}
//
////    public static File getDirectory(String variableName, String... paths) {
//fun getExternalSDCardDirectory(): File? {
//    val path = System.getenv("SECONDARY_STORAGE")
//    path?.let { p ->
//        if (!TextUtils.isEmpty(p)) {
//            if (p.contains(":")) {
//                for (_path in p.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
//                    val file = File(_path)
//                    if (file.exists()) {
//                        return file
//                    }
//                }
//            } else {
//                val file = File(p)
//                if (file.exists()) {
//                    return file
//                }
//            }
//        }
//    }
//    return null
//}
//
//fun getInternalDirectory(): File {
//    return Environment.getExternalStorageDirectory()
//}
//
//fun getDataDirectory(): File {
//    return File(Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID)
//}
//
//fun Activity.hideSoftKeyboard() {
//    try {
//        val inputMethodManager = this.getSystemService(
//            Activity.INPUT_METHOD_SERVICE
//        ) as InputMethodManager
//        inputMethodManager.hideSoftInputFromWindow(
//            this.currentFocus!!.windowToken, 0
//        )
//    } catch (e: Exception) {
//
//    }
//
//}
//
//fun showSoftKeyboard() {
//    val imm = ApplicationClass.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
//}
//
//fun copyToClipboard(text: String) {
//    val clipboard = ApplicationClass.getInstance().getSystemService(Application.CLIPBOARD_SERVICE) as android.content.ClipboardManager
//    val clip = ClipData.newPlainText(BuildConfig.APPLICATION_ID, text)
//    clipboard.setPrimaryClip(clip)
//
//}
//
//fun shortenNumber(number: Double, decimalCount: Int = 1): String {
//    val format = "%." + decimalCount + "f"
//    return when {
//        number < 1000       -> String.format(Locale.ENGLISH, format, number)
//        number < 1000000    -> String.format(Locale.ENGLISH, format, number / 1000) + "K"
//        number < 1000000000 -> String.format(Locale.ENGLISH, format, number / 1000000) + "M"
//        else                -> String.format(Locale.ENGLISH, format, number / 1000000000) + "B"
//    }
//}
//
//fun shortenNumber(number: Long, decimalCount: Int = 1): String {
//    val format = "%." + decimalCount + "f"
//    return when {
//        number < 1000       -> number.toString()
//        number < 1000000    -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000) + "K"
//        number < 1000000000 -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000000) + "M"
//        else                -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000000000) + "B"
//    }
//}
//
//fun separateNumberBy3(number: Long): String {
//    val formatter = DecimalFormat("#,###,###")
//    return formatter.format(number)
//}
//
//fun scanMedia(path: String) {
//    log("SCANNING=" + Uri.fromFile(File(path)))
//    MediaScannerConnection.scanFile(
//        ApplicationClass.getInstance(),
//        arrayOf(path),
//        null
//    ) { p, uri -> log("SCAN COMPLETE|PATH=$p|URI=$uri") }
//}
//
//fun <T : Any> extractModel(obj: T, output: String): String {
//    var result = output
//    try {
//        val fields = obj.javaClass.declaredFields
//        for (field in fields) {
//            field.isAccessible = true
//            val type = field.get(obj)
//            if (type is Int || type is String || type is Long || type is Double || type is Float
//                || type is Boolean
//            ) {
//                result += field.name + " : " + field.get(obj) + "\n"
//            } else {
//                extractModel(field.get(obj), result)
//            }
//            field.isAccessible = false
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return result
//}
//
//fun getStringLog(vararg s: Any?): String {
//    val value = StringBuilder()
//    for (item in s) {
//        value.append(item.toString()).append("\n")
//    }
//    return value.toString()
//}
//
//fun log(vararg s: Any?) {
//    if (BuildConfig.DEBUG || true) {
//        //        Log.e(getLocation(), getStringLog(*s))
//        val length = 3500
//        val location = getLocation()
//        var result = ""
//        s.forEach {
//            result += it.toString() + "\n"
//        }
//        while (result.isNotEmpty()) {
//            result = if (result.length >= length) {
//                Log.e(location, result.substring(0, length))
//                result.substring(length, result.length)
//            } else {
//                Log.e(location, result)
//                ""
//            }
//        }
//    }
//}
//
//fun logToFile(force: Boolean, fileName: String, vararg s: Any?) {
//    if (force || ApplicationClass.getInstance().prefManager.getBooleanPref(Constant.PREF_LOG)) {
//        try {
//            Thread(Runnable {
//                val file = File(
//                    Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID,
//                    "$fileName.txt"
//                )
//                var a = getStringFromFile(file)
//                val string = getStringLog(*s)
//                if (a == null) {
//                    a = ""
//                } else {
//                    if (a.length > 1000000) {
//                        a = a.substring(string.length)
//                    }
//                }
//                a += "\n\n\n" + getStackTrace() + "\n" + string
//                createFileFromString(
//                    a,
//                    Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID,
//                    "$fileName.txt"
//                )
//            }).start()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//}
//
///**
// * Symbol  Meaning                Kind         Example
// * D       day in year             Number        189
// * E       day of week             Text          E/EE/EEE:Tue, EEEE:Tuesday, EEEEE:T
// * F       day of week in month    Number        2 (2nd Wed in July)
// * G       era designator          Text          AD
// * H       hour in day (0-23)      Number        0
// * K       hour in am/pm (0-11)    Number        0
// * L       stand-alone month       Text          L:1 LL:01 LLL:Jan LLLL:January LLLLL:J
// * M       month in year           Text          M:1 MM:01 MMM:Jan MMMM:January MMMMM:J
// * S       fractional seconds      Number        978
// * W       week in month           Number        2
// * Z       time zone (RFC 822)     Time Zone     Z/ZZ/ZZZ:-0800 ZZZZ:GMT-08:00 ZZZZZ:-08:00
// * a       am/pm marker            Text          PM
// * c       stand-alone day of week Text          c/cc/ccc:Tue, cccc:Tuesday, ccccc:T
// * d       day in month            Number        10
// * h       hour in am/pm (1-12)    Number        12
// * k       hour in day (1-24)      Number        24
// * m       minute in hour          Number        30
// * s       second in minute        Number        55
// * w       week in year            Number        27
// * G       era designator          Text          AD
// * y       year                    Number        yy:10 y/yyy/yyyy:2010
// * z       time zone               Time Zone     z/zz/zzz:PST zzzz:Pacific Standard
// */
//fun convertTimestampToDate(timestamp: Long, dateFormat: String): String {
//    // Create a DateFormatter object for displaying date in specified format.
//    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
//
//    // Create a calendar object that will convert the date and time value in milliseconds to date.
//    val calendar = Calendar.getInstance()
//    calendar.timeInMillis = timestamp
//    return formatter.format(calendar.time)
//}
//
//
//fun convertDateToTimestamp(date: String, dateFormat: String = "yyyy/MM/dd hh:mm"): Long {
//    val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
//    return try {
//        sdf.parse(date).time
//    } catch (e: Exception) {
//        e.printStackTrace()
//        0
//    }
//}
//
//fun convertTimestampToCalendar(timestamp: Long): Calendar {
//    val d = Date(timestamp)
//    val c = Calendar.getInstance()
//    c.time = d
//    return c
//}
//
//
//fun convertBundleToString(bundle: Bundle?): String {
//    return bundle?.let {
//        var content = "Bundle :\n{\n"
//        for (key in it.keySet()) {
//            content += "\"" + key + "\":\"" + it.get(key) + "\",\n"
//        }
//        content.substring(0, content.length - 2) + "\n}"
//    } ?: run {
//        "{}"
//    }
//}
//
//fun convertBundleToJson(bundle: Bundle?): JSONObject {
//    val jsonObject = JSONObject()
//    try {
//        if (bundle != null) {
//            for (key in bundle.keySet()) {
//                jsonObject.put(key, bundle.get(key))
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return jsonObject
//}
//
////fun isGooglePlayServicesAvailable(activity: Activity, showDialog: Boolean): Boolean {
////    val googleApiAvailability = GoogleApiAvailability.getInstance()
////    val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
////    if (status != ConnectionResult.SUCCESS) {
////        if (googleApiAvailability.isUserResolvableError(status) && showDialog) {
////            googleApiAvailability.getErrorDialog(activity, status, 2404).show()
////        }
////        return false
////    }
////    return true
////}
//
//fun <T> convertJSONArrayToList(jsonArray: JSONArray): List<T> {
//    val list = ArrayList<T>()
//    try {
//        for (i in 0 until jsonArray.length()) {
//            list.add(jsonArray.get(i) as T)
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return list
//}
//
//fun <T> convertListToJSONArray(list: List<T>): JSONArray {
//    val jsonArray = JSONArray()
//    try {
//        for (i in list.indices) {
//            jsonArray.put(list[i])
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return jsonArray
//}
//
//fun setTextViewGradient(textView: TextView, firstColor: Int, secondColor: Int, center: Boolean) {
//    val shader: Shader
//    shader = if (center) {
//        RadialGradient(
//            (textView.width / 2).toFloat(), (textView.height / 2).toFloat(), (textView.width / 2).toFloat(),
//            firstColor, secondColor, Shader.TileMode.MIRROR
//        )
//    } else {
//        RadialGradient(
//            textView.x, textView.y, textView.width.toFloat(),
//            firstColor, secondColor, Shader.TileMode.MIRROR
//        )
//    }
//    textView.paint.shader = shader
//}
//
//fun getDominantColor(bitmap: Bitmap): Int {
//    val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
//    val color = newBitmap.getPixel(0, 0)
//    newBitmap.recycle()
//    return color
//}
//
//fun getDominantColor(resourceId: Int): Int {
//    return getDominantColor(Bitmap.createScaledBitmap(convertDrawableToBitmap(resourceId), 1, 1, true))
//}
//
//fun convertUriToPath(contentUri: Uri): String {
//    var cursor: Cursor? = null
//    try {
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        cursor = ApplicationClass.getInstance().contentResolver.query(contentUri, proj, null, null, null)
//        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(column_index)
//    } finally {
//        cursor?.close()
//    }
//}
//
//
//fun setEditTextCursorColor(view: EditText, @ColorInt color: Int) {
//    try {
//        // Get the cursor resource id
//        var field = TextView::class.java.getDeclaredField("mCursorDrawableRes")
//        field.isAccessible = true
//        val drawableResId = field.getInt(view)
//
//        // Get the editor
//        field = TextView::class.java.getDeclaredField("mEditor")
//        field.isAccessible = true
//        val editor = field.get(view)
//
//        // Get the drawable and set a color filter
//        val drawable: Drawable? = ContextCompat.getDrawable(view.context, drawableResId)
//        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
//        val drawables = drawable?.let { arrayOf(it, it) }
//
//        // Set the drawables
//        field = editor.javaClass.getDeclaredField("mCursorDrawable")
//        field.isAccessible = true
//        field.set(editor, drawables)
//    } catch (ignored: Exception) {
//    }
//
//}
//
//
//fun setEditTextCursor(view: EditText, @DrawableRes drawable: Int) {
//    try {
//        val fEditor = TextView::class.java.getDeclaredField("mEditor")
//        fEditor.isAccessible = true
//        val editor = fEditor.get(view)
//
//        val fSelectHandleLeft = editor.javaClass.getDeclaredField("mSelectHandleLeft")
//        val fSelectHandleRight = editor.javaClass.getDeclaredField("mSelectHandleRight")
//        val fSelectHandleCenter = editor.javaClass.getDeclaredField("mSelectHandleCenter")
//
//        fSelectHandleLeft.isAccessible = true
//        fSelectHandleRight.isAccessible = true
//        fSelectHandleCenter.isAccessible = true
//
//        fSelectHandleLeft.set(editor, drawable)
//        fSelectHandleRight.set(editor, drawable)
//        fSelectHandleCenter.set(editor, drawable)
//    } catch (ignored: Exception) {
//    }
//
//}
//
//
//fun setStatusbarColor(activity: Activity, color: Int) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        val window = activity.window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = color
//    }
//}
//
///**
// * @param factor must be less than 1. less is darker.
// */
//fun darkenColor(color: Int, factor: Float): Int {
//    val a = Color.alpha(color)
//    val r = Math.round(Color.red(color) * factor)
//    val g = Math.round(Color.green(color) * factor)
//    val b = Math.round(Color.blue(color) * factor)
//    return Color.argb(
//        a,
//        Math.min(r, 255),
//        Math.min(g, 255),
//        Math.min(b, 255)
//    )
//}
//
///**
// * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
// */
//fun isNetworkAvailable(): Boolean {
//    val connectivityManager =
//        ApplicationClass.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val activeNetworkInfo = connectivityManager.activeNetworkInfo
//    return activeNetworkInfo != null && activeNetworkInfo.isConnected
//}
//
//fun mergeJSONObjects(vararg jsonObject: JSONObject): JSONObject {
//    if (jsonObject.size < 2) {
//        return jsonObject[0]
//    } else {
//        var result = JSONObject()
//        for (aJsonObject in jsonObject) {
//            result = mergeJSONObjects(result, aJsonObject)
//        }
//        return result
//    }
//}
//
//fun mergeJSONObjects(jsonObject1: JSONObject, jsonObject2: JSONObject): JSONObject {
//    val merged = JSONObject()
//    try {
//        val objs = arrayOf(jsonObject1, jsonObject2)
//        for (obj in objs) {
//            val it = obj.keys()
//            while (it.hasNext()) {
//                val key = it.next() as String
//                merged.put(key, obj.get(key))
//            }
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//
//    return merged
//}
//
//fun isAppAvailable(appName: String): Boolean {
//    return try {
//        ApplicationClass.getInstance().packageManager.getPackageInfo(appName, 0)
//        true
//    } catch (e: PackageManager.NameNotFoundException) {
//        false
//    }
//
//}
//
//fun getColorFromAttributes(activity: Activity, color: Int): Int {
//    val typedValue = TypedValue()
//    val theme = activity.theme
//    theme.resolveAttribute(color, typedValue, true)
//    return typedValue.data
//}
//
//fun convertCelsiusToFahrenheit(temperatureInCelsius: Int): Int {
//    return (temperatureInCelsius * 1.8f).toInt() + 32
//}
//
//fun convertFahrenheitToCelsius(temperatureInFahrenheit: Int): Int {
//    return ((temperatureInFahrenheit - 32) / 1.8f).toInt()
//}
//
//
//fun animateTextViewText(
//    textView: TextView,
//    text: String,
//    delay: Int = 60,
//    startDelay: Long = 0,
//    onFinish: () -> Unit = {}
//) {
//    Handler(Looper.getMainLooper()).postDelayed({
//        for (i in 0 until text.length) {
//            Handler(Looper.getMainLooper()).postDelayed({ textView.text = text.substring(0, i + 1) }, (i * delay).toLong())
//        }
//        Handler(Looper.getMainLooper()).postDelayed({ onFinish() }, (text.length * delay).toLong())
//    }, startDelay)
//}
//
//
//fun animateEditTextHint(editText: EditText, text: String) {
//    animateEditTextHint(editText, text, 60)
//}
//
//fun animateEditTextHint(editText: EditText, text: String, delay: Int) {
//    for (i in 0 until text.length) {
//        Handler(Looper.getMainLooper()).postDelayed({ editText.hint = text.substring(0, i + 1) }, (i * delay).toLong())
//    }
//}
//
//fun Activity.shareImage(image: File?) {
//    if (image == null || !image.exists()) {
//        log("GlobalClass: shareImage: Error: image is not valid")
//        return
//    }
//    val share = Intent(Intent.ACTION_SEND)
//    share.type = "image/jpeg"
//    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image))
//    startActivity(Intent.createChooser(share, "Share To"))
//}
//
////fun Activity.shareImage(url: String?) {
////    if (url == null) {
////        log("GlobalClass: shareImage: Error: url is null")
////        return
////    }
////    val client = Client.getInstance()
////    client.request(API.Download(object : OnResponseListener {
////        override fun onResponse(response: Response) {
////            if (response.statusName === Response.StatusName.OK) {
////                shareImage(File(response.body))
////            } else {
////                log("GlobalClass: shareImage: Error: download image failed: code: " + response.statusCode + " url: " + response.request._url)
////            }
////        }
////
////        override fun onProgress(request: Request, bytesWritten: Long, totalSize: Long, percent: Int) {
////
////        }
////    }, url, getDataDirectory().path, "temp.jpg"))
////}
//
//fun validateJSON(jsonString: String): Boolean {
//    var isObject: Boolean
//    var isArray: Boolean
//    var jsonObject: JSONObject? = null
//    var jsonArray: JSONArray? = null
//    try {
//        jsonObject = JSONObject(jsonString)
//        isObject = true
//    } catch (e: JSONException) {
//        e.printStackTrace()
//        isObject = false
//    }
//
//    try {
//        jsonArray = JSONArray(jsonString)
//        isArray = true
//    } catch (e: JSONException) {
//        e.printStackTrace()
//        isArray = false
//    }
//
//    return if (isObject && jsonString.trim { it <= ' ' }.replace("\\", "").replace("/", "").replace(
//            " ",
//            ""
//        ).replace("\n", "").length == jsonObject!!.toString().replace("\\", "").replace(" ", "").replace(
//            "\n",
//            ""
//        ).replace("/", "").length
//    ) {
//        true
//    } else isArray && jsonString.trim { it <= ' ' }.replace("\\", "").replace("/", "").replace(" ", "").replace(
//        "\n",
//        ""
//    ).length == jsonArray!!.toString().replace("\\", "").replace("/", "").length
//}
//
//fun convertViewToBitmap(v: View, width: Int, height: Int): Bitmap {
//    val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//    val c = Canvas(b)
//    v.layout(0, 0, width, height)
//    v.draw(c)
//    return b
//}
//
//fun openGallery(activity: Activity, requestCode: Int, multiple: Boolean) {
//    val intent = Intent()
//    intent.type = "image/*"
//    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
//    intent.action = Intent.ACTION_GET_CONTENT
//    activity.startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode)
//}
//
//fun getIntentImages(data: Intent): ArrayList<Uri> {
//    val uriList = ArrayList<Uri>()
//    data.data?.let {
//        uriList.add(it)
//    } ?: run {
//        data.clipData?.let {
//            for (i in 0 until it.itemCount) {
//                uriList.add(it.getItemAt(i).uri)
//            }
//        }
//    }
//    return uriList
//}
//
//fun animateViews(views: Array<View?>, show: Boolean, duration: Long = 500, startDelay: Long = 0, delay: Long = 100, onFinish: () -> (Unit?) = {}) {
//    var time = 0L
//    var alpha = if (show) 1F else 0F
//    for (view in views) {
//        view?.animate()?.alpha(alpha)?.setStartDelay(startDelay + time)?.setDuration(duration)?.start()
//        time += delay
//    }
//    Handler(Looper.getMainLooper()).postDelayed({ onFinish() }, time + startDelay)
//}
//
//fun toast(message: String) {
//    Toast.makeText(ApplicationClass.getInstance(), message, Toast.LENGTH_SHORT).show()
//}
//
//fun toast(message: Int) {
//    Toast.makeText(ApplicationClass.getInstance(), ApplicationClass.getInstance().resources.getString(message), Toast.LENGTH_SHORT).show()
//}
//
//fun snack(view: View?, message: String, long: Boolean = false) {
//    view?.let {
//        val snack = Snackbar.make(it, message, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
//        val tv = snack.view.findViewById<TextView>(R.id.snackbar_text)
//        tv.typeface = ApplicationClass.getInstance().m.font
//        snack.show()
//    }
//}
//
//fun snack(view: View?, message: Int, long: Boolean = false) {
//    view?.let {
//        val snack = Snackbar.make(it, message, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
//        val tv = snack.view.findViewById<TextView>(R.id.snackbar_text)
//        tv.typeface = ApplicationClass.getInstance().m.font
//        snack.show()
//    }
//}
//
//fun transition(vararg viewPair: Pair<View, View>) { //todo : FAILED -> Reason : Constraints effect the animation
//    for (item in viewPair) {
//        transition(item.first, item.second)
//    }
//}
//
//fun transition(firstView: View, secondView: View, duration: Long = 500) { //todo : FAILED -> Reason : Constraints effect the animation
//
//    var initLeft = 0F
//    var initTop = 0F
//    var initHeight = 0
//    var initWidth = 0
//
//    var deltaLeft = 0F
//    var deltaTop = 0F
//    var deltaHeight = 0
//    var deltaWidth = 0
//
//    var initTextSize = 0F
//    var deltaTextSize = 0F
//
//    initLeft = firstView.x
//    deltaLeft = secondView.x - firstView.x
//
//    initTop = firstView.y
//    deltaTop = secondView.y - firstView.y
//
//
//    if (firstView is TextView && secondView is TextView) {
//        initTextSize = convertPXtoSP(firstView.textSize)
//        deltaTextSize = convertPXtoSP(secondView.textSize - firstView.textSize)
//    } else {
//        initHeight = firstView.height
//        deltaHeight = secondView.height - firstView.height
//
//        initWidth = firstView.width
//        deltaWidth = secondView.width - firstView.width
//    }
//
//
//    val animator = ValueAnimator.ofFloat(0F, 100F)
//    animator.addUpdateListener {
//        val percent = it.animatedValue as Float
//        firstView.x = ((percent * deltaLeft) / 100F) + initLeft
//        firstView.y = (percent * deltaTop) / 100F + initTop
//        if (firstView is TextView) {
//            firstView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ((percent * deltaTextSize) / 100F) + initTextSize)
//        } else {
//            val params = firstView.layoutParams
//            params.height = ((percent * deltaHeight) / 100F).toInt() + initHeight
//            params.width = ((percent * deltaWidth) / 100F).toInt() + initWidth
//            firstView.layoutParams = params
//        }
//    }
//    animator.interpolator = AccelerateInterpolator()
//    animator.duration = duration
//    animator.start()
//}
//
//operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
//    val value = this.value ?: arrayListOf()
//    value.addAll(values)
//    this.value = value
//}
//
//fun hasPermission(permission: String): Boolean {
//    val res = ApplicationClass.getInstance().checkCallingOrSelfPermission(permission)
//    return res == PackageManager.PERMISSION_GRANTED
//}
//
//fun ProgressBar.setTint(color: Int) {
//    indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
//}
//
//fun ViewGroup.changeChildFont(typeface: Typeface) {
//    for (i in 0 until childCount) {
//        val view = getChildAt(i)
//        if (view is TextView) {
//            view.typeface = ApplicationClass.getInstance().m.font
//        }
//    }
//}
//
//fun pasteClipboard(): String {
//    val clipboard = ApplicationClass.getInstance().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
//    var pasteData = ""
//
//    // If it does contain data, decide if you can handle the data.
//    if (!clipboard.hasPrimaryClip()) {
//    } else if (!clipboard.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//
//        // since the clipboard has data but it is not plain text
//    } else {
//
//        //since the clipboard contains plain text.
//        val item = clipboard.primaryClip!!.getItemAt(0)
//
//        // Gets the clipboard as text.
//        pasteData = item.text.toString()
//    }
//    return pasteData
//}
//
//fun Context.isServiceRunning(serviceClass: Class<Any>): Boolean {
//    val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
//        if (serviceClass.name == service.service.className) {
//            return true
//        }
//    }
//    return false
//}
//
//fun isPhonePluggedIn(): Boolean {
//    var charging = false
//
//    val batteryIntent: Intent? = ApplicationClass.getInstance().registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
//    val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
//    val batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING
//
//    val chargePlug = batteryIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
//    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
//    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
//
//    if (batteryCharge) charging = true
//    if (usbCharge) charging = true
//    if (acCharge) charging = true
//
//    return charging
//}
//
//fun getBatteryPercentage(): Int {
//    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
//    val batteryStatus = ApplicationClass.getInstance().registerReceiver(null, iFilter)
//
//    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
//    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
//
//    val batteryPct: Float = level / scale.toFloat()
//
//    return (batteryPct * 100).toInt()
//}
//
//
//fun String.substring(from: String, to: String): String? {
//    return if (contains(from)) {
//        val cut = substring(indexOf(from) + from.length)
//        return cut.substring(0, if (cut.indexOf(to) == -1) cut.length else cut.indexOf(to))
//    } else {
//        null
//    }
//}
//
//
//fun ViewGroup.animateChild() {
//    val views: Array<View?> = arrayOfNulls(childCount)
//    for (i in 0 until childCount) {
//        val view = getChildAt(i)
//        views[i] = view
//    }
//    animateViews(views, false, startDelay = 2000)
//}
//
//fun getRandomColor(contrast: Int = 500): Int {
//
//
//    val appClass = ApplicationClass.getInstance()
//    val colors: ArrayList<Int> = arrayListOf(
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_red_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_pink_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_purple_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_deep_purple_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_indigo_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_blue_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_light_blue_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_cyan_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_teal_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_green_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_light_green_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_lime_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_yellow_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_amber_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_orange_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_deep_orange_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_brown_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_grey_$contrast", "color", appClass.packageName)),
//        ContextCompat.getColor(appClass, appClass.resources.getIdentifier("md_blue_grey_$contrast", "color", appClass.packageName))
//    )
//    return colors[Random().nextInt(colors.size - 1)]
//}
//
//
//fun isColorDark(color: Int): Boolean {
//    val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
//    return darkness >= 0.5
//}
//
//fun TextView.underline() {
//    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
//}
//
//fun TextView.underlineRemove() {
//    paintFlags = paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
//}
//
//fun TextView.strikeThrough() {
//    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//}
//
//fun TextView.strikeThroughRemove() {
//    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//}
//
//fun TextView.bold() {
//    paintFlags = paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
//}
//
//fun TextView.boldRemove() {
//    paintFlags = paintFlags and Paint.FAKE_BOLD_TEXT_FLAG.inv()
//}
//
//
////fun <T : Any> Fragment.getBackStackData(key: String, result: (T) -> (Unit)) {
////    findNavController().currentBackStackEntry?.savedStateHandle?.apply {
////        getLiveData<T>(key).observe(viewLifecycleOwner) {
////            remove<T>(key)
////            result(it)
////        }
////    }
////}
////
////fun <T : Any> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = true) {
////    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
////    if (doBack)
////        findNavController().popBackStack()
////}
//
//fun isIgnoringBatteryOptimizations(): Boolean? {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        val context = ApplicationClass.getInstance()
//        val pwrm = context.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
//        val name = context.applicationContext.packageName
//        pwrm.isIgnoringBatteryOptimizations(name)
//    } else {
//        null
//    }
//}
//
//
