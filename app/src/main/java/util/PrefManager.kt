package util

import android.content.Context
import android.content.SharedPreferences
import main.ApplicationClass
import util.Constant.CON_AES_KEY
import util.Constant.CON_PREF_NAME

class PrefManager(appClass: ApplicationClass) {
    private val pref: SharedPreferences = appClass.getSharedPreferences(CON_PREF_NAME, Context.MODE_PRIVATE)

    fun getStringPref(key: String): String? {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                null
            } else {
                val result = decPref(item)
                result.ifBlank { null }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getBooleanPref(key: String): Boolean {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                false
            } else {
                decPref(item).toBoolean()
            }
        } catch (e: Exception) {
            false
        }
    }

    fun getIntPref(key: String): Int {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                0
            } else {
                decPref(item).toInt()
            }
        } catch (e: Exception) {
            0
        }
    }

    fun getLongPref(key: String): Long {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                0L
            } else {
                decPref(item).toLong()
            }
        } catch (e: Exception) {
            0L
        }
    }

    fun getFloatPref(key: String): Float {
        return try {
            val item = pref.getString(encPref(key), null)
            if (item == null) {
                0F
            } else {
                decPref(item).toFloat()
            }
        } catch (e: Exception) {
            0F
        }
    }

    fun setPref(key: String, value: Any) {
        pref.edit().putString(encPref(key), encPref(value)).apply()
    }

    private fun encPref(value: Any): String {
        return MCryptAES(CON_AES_KEY).encrypt(value.toString())
    }

    private fun decPref(value: Any): String {
        return MCryptAES(CON_AES_KEY).decrypt(value.toString())
    }
}