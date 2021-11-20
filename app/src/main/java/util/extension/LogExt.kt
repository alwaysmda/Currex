package util.extension

import android.os.Environment
import android.util.Log
import com.xodus.templatefive.BuildConfig
import main.ApplicationClass
import util.Constant
import java.io.File

fun getLocation(): String {
    val CALL_STACK_INDEX = 2
    // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
    // because Robolectric runs them on the JVM but on Android the elements are different.
    val stackTrace = Throwable().stackTrace
    if (stackTrace.size <= CALL_STACK_INDEX) {
        throw IllegalStateException(
            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
        )
    }

    var element: StackTraceElement = stackTrace[2]
    for (item in stackTrace) {
        if (item.methodName != "getLocation" && item.methodName != "log") {
            element = item
            break
        }
    }

    return ".(" + element.toString().substring(
        element.toString().lastIndexOf(
            "("
        ) + 1, element.toString().lastIndexOf(")")
    ) + ")"
}

fun getLocation(index: Int): String {
    val stackTrace = Throwable().stackTrace
    if (stackTrace.size <= index) {
        throw IllegalStateException(
            "Synthetic stacktrace didn't have enough elements: are you using proguard?"
        )
    }
    //        for (int i = 0; i < stackTrace.length; i++) {
    //            log(LOG, "TRACETRACE INDEX=" + i + "|" + stackTrace[i].toString());
    //        }


    return ".(" + stackTrace[index].toString().substring(
        stackTrace[index].toString().lastIndexOf("(") + 1,
        stackTrace[index].toString().lastIndexOf(")")
    ) + ")"
}

fun getStackTrace(): String {
    val stackTrace = Throwable().stackTrace
    var stack = ""
    for (i in stackTrace.indices) {
        stack += "\n.(" + stackTrace[i].toString().substring(
            stackTrace[i].toString().lastIndexOf("(") + 1,
            stackTrace[i].toString().lastIndexOf(")")
        ) + ")"
    }
    return stack
}

fun getStringLog(vararg s: Any?): String {
    val value = StringBuilder()
    for (item in s) {
        value.append(item.toString()).append("\n")
    }
    return value.toString()
}

fun log(vararg s: Any?) {
    if (BuildConfig.DEBUG || true) {
        //        Log.e(getLocation(), getStringLog(*s))
        val length = 3500
        val location = getLocation()
        var result = ""
        s.forEach {
            result += it.toString() + "\n"
        }
        while (result.isNotEmpty()) {
            result = if (result.length >= length) {
                Log.e(location, result.substring(0, length))
                result.substring(length, result.length)
            } else {
                Log.e(location, result)
                ""
            }
        }
    }
}

fun logToFile(force: Boolean, fileName: String, vararg s: Any?) {
    if (force || ApplicationClass.getInstance().prefManager.getBooleanPref(Constant.PREF_LOG)) {
        try {
            Thread(Runnable {
                val file = File(
                    Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID,
                    "$fileName.txt"
                )
                var a = getStringFromFile(file)
                val string = getStringLog(*s)
                if (a == null) {
                    a = ""
                } else {
                    if (a.length > 1000000) {
                        a = a.substring(string.length)
                    }
                }
                a += "\n\n\n" + getStackTrace() + "\n" + string
                createFileFromString(
                    a,
                    Environment.getExternalStorageDirectory().path + "/Android/data/" + BuildConfig.APPLICATION_ID,
                    "$fileName.txt"
                )
            }).start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}