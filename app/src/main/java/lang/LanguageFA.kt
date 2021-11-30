package lang

import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.xodus.templatefive.R
import main.ApplicationClass

class LanguageFA(private val app: ApplicationClass) : LanguageInterface {
    override val fontLight: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_fa_light)
    override val fontMedium: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_fa_medium)
    override val fontBold: Typeface? get() = ResourcesCompat.getFont(app, R.font.font_fa_bold)
    override val locale = "FA"
    override val appName = "نمونه پنجم"
    override val openUrl = "نمایش در مرورگر"
    override val nothingToShow = "چیزی برای نمایش پیدا نشد!"
    override val checkConnectionAndTryAgain = "لطفا از اتصال به اینترنت اطمینان حاصل کنید و دوباره امتحان کنید."
    override val retry = "تلاش مجدد"
    override val id = "آیدی"
    override val albumId = "آیدی آلبوم"
    override val title = "تیتر"
    override val languageUpdated = "زبان تغییر کرد"
    override val somethingWentWrong = "مشکلی پیش آمده"
    override val pressBackAgainToExit = "برای خروج دوباره بزنید"
    override val loading = "در حال بارگذاری"
    override val download = "دانلود"
}
