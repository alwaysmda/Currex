package util.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.provider.MediaStore
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.xodus.templatefive.BuildConfig
import com.xodus.templatefive.R
import main.ApplicationClass
import java.io.File
import java.util.*

@SuppressLint("HardwareIds")
fun getAndroidID(): String {
    return Settings.Secure.getString(ApplicationClass.getInstance().contentResolver, Settings.Secure.ANDROID_ID)
}


fun getUUID(name: String? = null): String {
    return UUID.fromString(name).toString()
}


fun getPackageInfo(): PackageInfo {
    return ApplicationClass.getInstance().packageManager.getPackageInfo(ApplicationClass.getInstance().packageName, 0)
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

private fun getSoftButtonsBarHeight(activity: Activity): Int {
    // getRealMetrics is only available with API 17 and +
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight)
            realHeight - usableHeight
        else
            0
    }
    return 0
}

fun getStatusBarHeight(): Int {
    var result = 0
    val resourceId = ApplicationClass.getInstance().resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = ApplicationClass.getInstance().resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun getScreenDensity(): Float {
    return ApplicationClass.getInstance().resources.displayMetrics.density
}

fun Activity.hideSoftKeyboard() {
    try {
        val inputMethodManager = this.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken, 0
        )
    } catch (e: Exception) {

    }

}

fun showSoftKeyboard() {
    val imm = ApplicationClass.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun copyToClipboard(text: String) {
    val clipboard = ApplicationClass.getInstance().getSystemService(Application.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = ClipData.newPlainText(BuildConfig.APPLICATION_ID, text)
    clipboard.setPrimaryClip(clip)
}


fun convertUriToPath(contentUri: Uri): String {
    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = ApplicationClass.getInstance().contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}


fun setStatusbarColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}


/**
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 */
fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        ApplicationClass.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


fun isAppAvailable(appName: String): Boolean {
    return try {
        ApplicationClass.getInstance().packageManager.getPackageInfo(appName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

}

fun getColorFromAttributes(activity: Activity, color: Int): Int {
    val typedValue = TypedValue()
    val theme = activity.theme
    theme.resolveAttribute(color, typedValue, true)
    return typedValue.data
}


fun openGallery(activity: Activity, requestCode: Int, multiple: Boolean) {
    val intent = Intent()
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple)
    intent.action = Intent.ACTION_GET_CONTENT
    activity.startActivityForResult(Intent.createChooser(intent, "Select Image"), requestCode)
}

fun getIntentImages(data: Intent): ArrayList<Uri> {
    val uriList = ArrayList<Uri>()
    data.data?.let {
        uriList.add(it)
    } ?: run {
        data.clipData?.let {
            for (i in 0 until it.itemCount) {
                uriList.add(it.getItemAt(i).uri)
            }
        }
    }
    return uriList
}

fun toast(message: String) {
    Toast.makeText(ApplicationClass.getInstance(), message, Toast.LENGTH_SHORT).show()
}

fun toast(message: Int) {
    Toast.makeText(ApplicationClass.getInstance(), ApplicationClass.getInstance().resources.getString(message), Toast.LENGTH_SHORT).show()
}

fun snack(view: View?, message: String, long: Boolean = false) {
    view?.let {
        val snack = Snackbar.make(it, message, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        val tv = snack.view.findViewById<TextView>(R.id.snackbar_text)
        tv.typeface = ApplicationClass.getInstance().m.fontLight
        snack.show()
    }
}

fun snack(view: View?, message: Int, long: Boolean = false) {
    view?.let {
        val snack = Snackbar.make(it, message, if (long) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        val tv = snack.view.findViewById<TextView>(R.id.snackbar_text)
        tv.typeface = ApplicationClass.getInstance().m.fontLight
        snack.show()
    }
}

fun Context.isServiceRunning(serviceClass: Class<Any>): Boolean {
    val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun isPhonePluggedIn(): Boolean {
    var charging = false

    val batteryIntent: Intent? = ApplicationClass.getInstance().registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
    val batteryCharge = status == BatteryManager.BATTERY_STATUS_CHARGING

    val chargePlug = batteryIntent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
    val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
    val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC

    if (batteryCharge) charging = true
    if (usbCharge) charging = true
    if (acCharge) charging = true

    return charging
}

fun getBatteryPercentage(): Int {
    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus = ApplicationClass.getInstance().registerReceiver(null, iFilter)

    val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

    val batteryPct: Float = level / scale.toFloat()

    return (batteryPct * 100).toInt()
}

fun <T : Any> Fragment.getBackStackData(key: String, result: (T) -> (Unit)) {
    findNavController().currentBackStackEntry?.savedStateHandle?.apply {
        getLiveData<T>(key).observe(viewLifecycleOwner) {
            remove<T>(key)
            result(it)
        }
    }
}

fun <T : Any> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = true) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
    if (doBack)
        findNavController().popBackStack()
}

fun isIgnoringBatteryOptimizations(): Boolean? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val context = ApplicationClass.getInstance()
        val pwrm = context.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        val name = context.applicationContext.packageName
        pwrm.isIgnoringBatteryOptimizations(name)
    } else {
        null
    }
}

fun hasPermission(permission: String): Boolean {
    val res = ApplicationClass.getInstance().checkCallingOrSelfPermission(permission)
    return res == PackageManager.PERMISSION_GRANTED
}

fun Activity.shareImage(image: File?) {
    if (image == null || !image.exists()) {
        log("GlobalClass: shareImage: Error: image is not valid")
        return
    }
    val share = Intent(Intent.ACTION_SEND)
    share.type = "image/jpeg"
    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image))
    startActivity(Intent.createChooser(share, "Share To"))
}

//fun Activity.shareImage(url: String?) {
//    if (url == null) {
//        log("GlobalClass: shareImage: Error: url is null")
//        return
//    }
//    val client = Client.getInstance()
//    client.request(API.Download(object : OnResponseListener {
//        override fun onResponse(response: Response) {
//            if (response.statusName === Response.StatusName.OK) {
//                shareImage(File(response.body))
//            } else {
//                log("GlobalClass: shareImage: Error: download image failed: code: " + response.statusCode + " url: " + response.request._url)
//            }
//        }
//
//        override fun onProgress(request: Request, bytesWritten: Long, totalSize: Long, percent: Int) {
//
//        }
//    }, url, getDataDirectory().path, "temp.jpg"))
//}

fun pasteClipboard(): String {
    val clipboard = ApplicationClass.getInstance().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    var pasteData = ""

    // If it does contain data, decide if you can handle the data.
    if (!clipboard.hasPrimaryClip()) {
    } else if (!clipboard.primaryClipDescription!!.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

        // since the clipboard has data but it is not plain text
    } else {

        //since the clipboard contains plain text.
        val item = clipboard.primaryClip!!.getItemAt(0)

        // Gets the clipboard as text.
        pasteData = item.text.toString()
    }
    return pasteData
}

//fun isGooglePlayServicesAvailable(activity: Activity, showDialog: Boolean): Boolean {
//    val googleApiAvailability = GoogleApiAvailability.getInstance()
//    val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
//    if (status != ConnectionResult.SUCCESS) {
//        if (googleApiAvailability.isUserResolvableError(status) && showDialog) {
//            googleApiAvailability.getErrorDialog(activity, status, 2404).show()
//        }
//        return false
//    }
//    return true
//}

fun convertPXtoSP(px: Float): Float {
    return px / ApplicationClass.getInstance().resources.displayMetrics.scaledDensity
}

fun convertDPtoPX(dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ApplicationClass.getInstance().resources.displayMetrics)
}

fun convertPXtoDP(px: Float): Float {
    return px / (ApplicationClass.getInstance().resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun convertSPtoPX(sp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ApplicationClass.getInstance().resources.displayMetrics)
        .toInt()
}

fun convertDPtoSP(dp: Float): Int {
    return (convertDPtoPX(dp) / ApplicationClass.getInstance().resources.displayMetrics.scaledDensity).toInt()
}