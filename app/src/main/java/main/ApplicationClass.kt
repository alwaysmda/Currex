package main

import android.app.Application
import billing.Market
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.hilt.android.HiltAndroidApp
import lang.LanguageInterface
import lang.LanguageManager
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import util.Constant
import util.GoogleBillingV4Helper
import util.NativeBillingV4Helper
import util.PrefManager
import util.ThemeManager
import util.extension.log
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class ApplicationClass : Application() {

    lateinit var googleBillingHelper: GoogleBillingV4Helper
    lateinit var nativeBillingHelper: NativeBillingV4Helper

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var languageManager: LanguageManager

    @Inject
    lateinit var market: Market

    val m: LanguageInterface
        get() = languageManager.main

    companion object {
        @Volatile
        private lateinit var instance: ApplicationClass
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        languageManager.changeLang(Constant.Languages.EN)
        setupPicasso()
    }

    private fun setupPicasso() {
        val client = OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
            .build()
        val picasso = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(client))
            .listener { picasso, uri, exception ->
                log("PICASSO EXCEPTION", uri, exception.localizedMessage, exception.printStackTrace())
            }
            .build()
        Picasso.setSingletonInstance(picasso)
    }
}
