package util

import android.app.Activity
import javax.inject.Inject


class ThemeManager @Inject constructor(private val prefManager: PrefManager) {
    val themeIsDark
        get() = currentTheme.value.contains("DARK")
    var currentTheme = Constant.Themes.LIGHT_PINK
    fun changeTheme(activity: Activity, theme: Constant.Themes) {
        prefManager.setPref(Constant.PREF_THEME, theme.value)
        currentTheme = theme
        activity.recreate()
    }
}