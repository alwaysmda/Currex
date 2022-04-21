package main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import util.Constant
import util.PrefManager
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass : Application() {
    @Inject
    lateinit var prefManager: PrefManager

    companion object {
        @Volatile
        private lateinit var instance: ApplicationClass
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDefaultPref()
    }

    private fun initDefaultPref() {
        if (prefManager.getBooleanPref(Constant.PREF_INITIALIZED).not()) {
            prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, Constant.CON_FREE_CONVERT_COUNT)
            prefManager.setPref(Constant.PREF_INITIALIZED, true)
        }
    }
}


/*
+ check internet connection, corrupted data, retry in 5 seconds
+ return rates only
+ minimum convert value cannot be lower than fee
+ calculate fee before convert and check balance
+ coordinator and search in balance list and currency list
+ rate update is not needed in balance list and currency list
+ check if rate list has at least 2 items
- use room
- show last update
+ show remaining free conversion count
+ show expiration error on request fail
+ pass error code from api and convert to error string later
- add select currency page
- error animation
- recyclerview animation
- edittext length limit
- add developer setting
    - reset balance
    - initial balance (name and value)
    - refresh interval
    - retry interval
    - conversion fee
    - free fee in first x conversion
    - free fee every x conversion
    - reduce fee from source
    - reduce fee from target
    - show fee before convert
    - homepage balance item count
    - contact developer
* */
