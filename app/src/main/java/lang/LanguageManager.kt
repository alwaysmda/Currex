package lang

import main.ApplicationClass
import util.Constant
import util.PrefManager
import javax.inject.Inject

class LanguageManager @Inject constructor(app: ApplicationClass, private val prefManager: PrefManager) {
    lateinit var main: LanguageInterface
    private val en: LanguageInterface = LanguageEN(app)
    private val fa: LanguageInterface = LanguageFA(app)

    var currentLanguage = Constant.Languages.DEFAULT_LANGUAGE
    fun changeLang(lang: Constant.Languages) {
        currentLanguage = lang
        prefManager.setPref(Constant.PREF_LANGUAGE, lang.value)
        main = when (lang) {
            Constant.Languages.DEFAULT_LANGUAGE,
            Constant.Languages.FA -> fa
            Constant.Languages.EN -> en
        }
    }
}