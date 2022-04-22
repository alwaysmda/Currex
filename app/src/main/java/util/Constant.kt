package util

object Constant {
    //========================== P R E F E R E N C E S ==========================//
    const val PREF_LOG = "PREF_LOG"
    const val PREF_SELL = "PREF_SELL"
    const val PREF_RECEIVE = "PREF_RECEIVE"
    const val PREF_INITIALIZED = "PREF_INITIALIZED"
    const val PREF_CONVERT_COUNT = "PREF_CONVERT_COUNT"
    const val PREF_RETRY_INTERVAL = "PREF_RETRY_INTERVAL"
    const val PREF_FREE_CONVERT_COUNT = "PREF_FREE_CONVERT_COUNT"
    const val PREF_FREE_CONVERT_EVERY_X = "PREF_FREE_CONVERT_EVERY_X"
    const val PREF_REFRESH_INTERVAL = "PREF_REFRESH_INTERVAL"
    const val PREF_HOME_BALANCE_COUNT = "PREF_HOME_BALANCE_COUNT"
    const val PREF_INITIAL_BALANCE_VALUE = "PREF_INITIAL_BALANCE_VALUE"
    const val PREF_INITIAL_BALANCE_NAME = "PREF_INITIAL_BALANCE_NAME"
    const val PREF_CONVERSION_FEE = "PREF_CONVERSION_FEE"
    const val PREF_REDUCE_FEE_FROM_SOURCE = "PREF_REDUCE_FEE_FROM_SOURCE"
    const val PREF_REDUCE_FEE_FROM_TARGET = "PREF_REDUCE_FEE_FROM_TARGET"

    //========================== C O N S T A N T S ==========================//
    const val CON_BASE_URL = "http://api.exchangeratesapi.io/v1/"
    const val CON_API_ACCESS_KEY = "4166b82d72c51e9c96606344f3431234"
    const val CON_AES_KEY = "dde717bc4fd78bbbd98ccc7d8516ba79"
    const val CON_AES_IV = "a3da2dab4e2b44d1"
    const val CON_RETRY_INTERVAL = 5
    const val CON_FREE_CONVERT_COUNT = 5
    const val CON_FREE_CONVERT_EVERY_X = 0
    const val CON_REFRESH_INTERVAL = 5
    const val CON_HOME_BALANCE_COUNT = 10
    const val CON_INITIAL_BALANCE_VALUE = 1000.0
    const val CON_INITIAL_BALANCE_NAME = "EUR"
    const val CON_CONVERSION_FEE = 0.007
    const val CON_REDUCE_FEE_FROM_SOURCE = true
    const val CON_REDUCE_FEE_FROM_TARGET = true

    //========================== A R G S ==========================//
    const val ARG_RATE = "ARG_RATE"
}
