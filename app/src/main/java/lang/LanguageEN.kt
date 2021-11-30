package lang

import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.xodus.templatefive.R
import main.ApplicationClass

class LanguageEN(private val app: ApplicationClass) : LanguageInterface {
    override val fontLight: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_en_light)
    override val fontMedium: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_en_light)
    override val fontBold: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_en_medium)
    override val locale = "EN"
    override val appName = "Template Five"
    override val openUrl = "Open URL"
    override val nothingToShow = "Nothing To Show!"
    override val checkConnectionAndTryAgain = "Please check your internet connection and try again."
    override val retry = "Retry"
    override val id = "ID"
    override val albumId = "Album ID"
    override val title = "Title"
    override val languageUpdated = "Language Updated"
    override val somethingWentWrong = "Something Went Wrong"
    override val pressBackAgainToExit = "Press back again to exit"
    override val loading = "Loading..."
    override val download = "Download"

}
