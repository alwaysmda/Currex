package lang

import android.graphics.Typeface

interface LanguageInterface {
    val fontLight: Typeface?
    val fontMedium: Typeface?
    val fontBold: Typeface?

    val locale: String
    val appName: String
    val openUrl: String
    val nothingToShow: String
    val checkConnectionAndTryAgain: String
    val retry: String
    val id: String
    val albumId: String
    val title: String
    val languageUpdated: String
    val somethingWentWrong: String
    val pressBackAgainToExit: String
    val loading: String
    val download: String

    fun paramString(param: String) = "$appName $param"
}