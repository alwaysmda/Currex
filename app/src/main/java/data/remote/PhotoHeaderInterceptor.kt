package data.remote

import android.content.Context
import android.telephony.TelephonyManager
import androidx.core.content.pm.PackageInfoCompat
import billing.Market
import dagger.hilt.android.qualifiers.ApplicationContext
import lang.LanguageManager
import main.ApplicationClass
import okhttp3.Interceptor
import okhttp3.Response
import util.extension.getAndroidID
import util.extension.getPackageInfo
import javax.inject.Inject

class PhotoHeaderInterceptor @Inject constructor(@ApplicationContext private val appClass: ApplicationClass, private val market: Market, private val languageManager: LanguageManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            addHeader("Device-Id", getAndroidID())
            addHeader(
                "User-Agent",
                "TemplateFive"
                        + " "
                        + getPackageInfo().versionName
                        + " (Android; "
                        + PackageInfoCompat.getLongVersionCode(getPackageInfo()) + "; "
                        + market.name + "; "
                        + (appClass.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso + "; "
                        + getAndroidID()
                        + "; "
                        + languageManager.currentLanguage.value + ")"
            )
        }.build()
        return chain.proceed(request)
    }
}