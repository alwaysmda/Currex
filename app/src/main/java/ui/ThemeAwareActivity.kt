package ui

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.xodus.templatefive.R
import dagger.hilt.android.AndroidEntryPoint
import main.ApplicationClass
import util.Constant
import javax.inject.Inject

@AndroidEntryPoint
open class ThemeAwareActivity : AppCompatActivity() {

    @Inject
    lateinit var appClass: ApplicationClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getCurrentThemeId())
        AppCompatDelegate.setDefaultNightMode(if (appClass.themeManager.themeIsDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            window.navigationBarColor = ContextCompat.getColor(baseContext, if (themeManager.themeIsDark) R.color.md_black_1000 else R.color.md_white_1000)
        //        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = ContextCompat.getColor(baseContext, if (appClass.themeManager.themeIsDark) R.color.md_white_1000 else R.color.md_black_1000)
        }
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(getCurrentThemeId(), true)
        return theme
    }

    private fun getCurrentThemeId(): Int {
        //todo
        return when (appClass.themeManager.currentTheme) {
            Constant.Themes.LIGHT_PINK -> R.style.AppTheme_Light_Pink
            Constant.Themes.DARK_BLUE  -> R.style.AppTheme_Dark_Blue
            else                       -> R.style.AppTheme_Light_Pink
        }
    }
}