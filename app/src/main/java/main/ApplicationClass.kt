package main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
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
    }
}
