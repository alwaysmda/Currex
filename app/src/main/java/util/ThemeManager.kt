package util

import javax.inject.Inject


class ThemeManager @Inject constructor(private val prefManager: PrefManager) {
    val themeIsDark
        get() = currentTheme.value.contains("DARK")
    var currentTheme = Constant.Themes.LIGHT_PINK
    fun changeTheme(theme: Constant.Themes) {
        prefManager.setPref(Constant.PREF_THEME, theme.value)
        currentTheme = theme
    }
}