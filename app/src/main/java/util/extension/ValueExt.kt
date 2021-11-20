package util.extension

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import main.ApplicationClass
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import util.Constant
import java.security.MessageDigest
import java.text.DecimalFormat
import java.util.*

fun getRandomString(length: Int): String {
    val AB = "0123456789abcdefghijklmnopqrstuvwxyz"
    val rnd = Random()
    val sb = StringBuilder(length)
    for (i in 0 until length)
        sb.append(AB[rnd.nextInt(AB.length)])
    return sb.toString()
}

fun getRandomInt(length: Int): Int {
    val AB = "0123456789"
    val rnd = Random()
    val sb = StringBuilder(length)
    for (i in 0 until length)
        sb.append(AB[rnd.nextInt(AB.length)])
    return Integer.valueOf(sb.toString())
}

fun translate(_c: Any): String {
    var c = _c.toString()
    val enN = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    val faN = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")

    if (ApplicationClass.getInstance().languageManager.currentLanguage.value == Constant.Languages.FA.value) {
        for (i in 0..9) {
            c = c.replace(enN[i], faN[i])
        }
    } else {
        for (i in 0..9) {
            c = c.replace(faN[i], enN[i])
        }
    }
    return c
}

fun shortenNumber(number: Double, decimalCount: Int = 1): String {
    val format = "%." + decimalCount + "f"
    return when {
        number < 1000       -> String.format(Locale.ENGLISH, format, number)
        number < 1000000    -> String.format(Locale.ENGLISH, format, number / 1000) + "K"
        number < 1000000000 -> String.format(Locale.ENGLISH, format, number / 1000000) + "M"
        else                -> String.format(Locale.ENGLISH, format, number / 1000000000) + "B"
    }
}

fun shortenNumber(number: Long, decimalCount: Int = 1): String {
    val format = "%." + decimalCount + "f"
    return when {
        number < 1000       -> number.toString()
        number < 1000000    -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000) + "K"
        number < 1000000000 -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000000) + "M"
        else                -> String.format(Locale.ENGLISH, format, number.toDouble() / 1000000000) + "B"
    }
}

fun separateNumberBy3(number: Long): String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(number)
}

fun convertBundleToString(bundle: Bundle?): String {
    return bundle?.let {
        var content = "Bundle :\n{\n"
        for (key in it.keySet()) {
            content += "\"" + key + "\":\"" + it.get(key) + "\",\n"
        }
        content.substring(0, content.length - 2) + "\n}"
    } ?: run {
        "{}"
    }
}

fun convertBundleToJson(bundle: Bundle?): JSONObject {
    val jsonObject = JSONObject()
    try {
        if (bundle != null) {
            for (key in bundle.keySet()) {
                jsonObject.put(key, bundle.get(key))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonObject
}

fun <T> convertJSONArrayToList(jsonArray: JSONArray): List<T> {
    val list = ArrayList<T>()
    try {
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.get(i) as T)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return list
}

fun <T> convertListToJSONArray(list: List<T>): JSONArray {
    val jsonArray = JSONArray()
    try {
        for (i in list.indices) {
            jsonArray.put(list[i])
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return jsonArray
}

fun <T : Any> extractModel(obj: T, output: String): String {
    var result = output
    try {
        val fields = obj.javaClass.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val type = field.get(obj)
            if (type is Int || type is String || type is Long || type is Double || type is Float
                || type is Boolean
            ) {
                result += field.name + " : " + field.get(obj) + "\n"
            } else {
                extractModel(field.get(obj), result)
            }
            field.isAccessible = false
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun mergeJSONObjects(vararg jsonObject: JSONObject): JSONObject {
    if (jsonObject.size < 2) {
        return jsonObject[0]
    } else {
        var result = JSONObject()
        for (aJsonObject in jsonObject) {
            result = mergeJSONObjects(result, aJsonObject)
        }
        return result
    }
}

fun mergeJSONObjects(jsonObject1: JSONObject, jsonObject2: JSONObject): JSONObject {
    val merged = JSONObject()
    try {
        val objs = arrayOf(jsonObject1, jsonObject2)
        for (obj in objs) {
            val it = obj.keys()
            while (it.hasNext()) {
                val key = it.next() as String
                merged.put(key, obj.get(key))
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return merged
}

fun String.substring(from: String, to: String): String? {
    return if (contains(from)) {
        val cut = substring(indexOf(from) + from.length)
        return cut.substring(0, if (cut.indexOf(to) == -1) cut.length else cut.indexOf(to))
    } else {
        null
    }
}

fun getMCryptAESKey(password: String): String? {
    var keyString: String? = null
    try {
        var key = password.toByteArray(Charsets.UTF_8)
        val sha = MessageDigest.getInstance("SHA-1")
        key = sha.digest(key)
        key = Arrays.copyOf(key, 16) // use only first 128 bit
        keyString = String(key, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return keyString
}

//fun HMACSHA256(key: String, value: String): String? {
//    var result: String? = null
//    try {
//        val keyBytes = key.toByteArray()
//        val signingKey = SecretKeySpec(keyBytes, "HmacSHA256")
//        val mac = Mac.getInstance("HmacSHA256")
//        mac.init(signingKey)
//        val rawHmac = mac.doFinal(value.toByteArray())
//        val hexBytes = Hex().encode(rawHmac)
//        result = String(hexBytes, Charsets.UTF_8)
//    } catch (e: Exception) {
//        log(e.message)
//    }
//    return result
//}

fun convertCelsiusToFahrenheit(temperatureInCelsius: Int): Int {
    return (temperatureInCelsius * 1.8f).toInt() + 32
}

fun convertFahrenheitToCelsius(temperatureInFahrenheit: Int): Int {
    return ((temperatureInFahrenheit - 32) / 1.8f).toInt()
}


fun validateJSON(jsonString: String): Boolean {
    var isObject: Boolean
    var isArray: Boolean
    var jsonObject: JSONObject? = null
    var jsonArray: JSONArray? = null
    try {
        jsonObject = JSONObject(jsonString)
        isObject = true
    } catch (e: JSONException) {
        e.printStackTrace()
        isObject = false
    }

    try {
        jsonArray = JSONArray(jsonString)
        isArray = true
    } catch (e: JSONException) {
        e.printStackTrace()
        isArray = false
    }

    return if (isObject && jsonString.trim { it <= ' ' }.replace("\\", "").replace("/", "").replace(
            " ",
            ""
        ).replace("\n", "").length == jsonObject!!.toString().replace("\\", "").replace(" ", "").replace(
            "\n",
            ""
        ).replace("/", "").length
    ) {
        true
    } else isArray && jsonString.trim { it <= ' ' }.replace("\\", "").replace("/", "").replace(" ", "").replace(
        "\n",
        ""
    ).length == jsonArray!!.toString().replace("\\", "").replace("/", "").length
}

operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}