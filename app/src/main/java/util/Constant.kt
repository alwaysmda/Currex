package util

object Constant {
    //========================== P R E F E R E N C E S ==========================//
    const val PREF_LANGUAGE = "PREF_LANGUAGE"
    const val PREF_THEME = "PREF_THEME"
    const val PREF_LOG = "PREF_LOG"

    //========================== C O N S T A N T S ==========================//
    const val CON_BASE_TEMPLATE_URL = "https://jsonplaceholder.typicode.com/"
    const val CON_PREF_NAME = "com.xodus.templatefive"
    const val CON_AES_KEY = "dde717bc4fd78bbbd98ccc7d8516ba79"
    const val CON_AES_IV = "a3da2dab4e2b44d1"
    const val CON_TRANS_ID = "CON_TRANS_ID_"
    const val CON_TRANS_PHOTO = "CON_TRANS_PHOTO_"

    enum class Languages(val value: String) {
        FA("FA"),
        EN("EN"),
        DEFAULT_LANGUAGE("EN"),
    }

    enum class Themes(val value: String) {
        DARK_BLUE("DARK_BLUE"),
        LIGHT_PINK("LIGHT_PINK"),
        DEFAULT_THEME("DARK_BLUE"),
    }
}