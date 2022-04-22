package util

class AppSetting(private val prefManager: PrefManager) {
    var retryInterval = 0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_RETRY_INTERVAL, value)
        }

    var freeConvert = false
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_FREE_CONVERT, value)
        }

    var freeConvertCount = 0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, value)
        }

    var freeConvertEveryX = 0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_FREE_CONVERT_EVERY_X, value)
        }

    var freeConvertBelowXEur = 0.0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_FREE_CONVERT_BELOW_X_EUR, value)
        }

    var refreshRegularly = false
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_REFRESH_REGULARLY, value)
        }
    var refreshInterval = 0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_REFRESH_INTERVAL, value)
        }

    var homeBalanceCount = 0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_HOME_BALANCE_COUNT, value)
        }

    var initialBalanceValue = 0.0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_INITIAL_BALANCE_VALUE, value)
        }

    var initialBalanceName = ""
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_INITIAL_BALANCE_NAME, value)
        }

    var conversionFee = 0.0
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_CONVERSION_FEE, value)
        }

    var reduceFeeFromSource = false
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_REDUCE_FEE_FROM_SOURCE, value)
        }

    var reduceFeeFromTarget = false
        set(value) {
            field = value
            prefManager.setPref(Constant.PREF_REDUCE_FEE_FROM_TARGET, value)
        }


    fun init() {
        if (prefManager.getBooleanPref(Constant.PREF_INITIALIZED).not()) {
            prefManager.setPref(Constant.PREF_RETRY_INTERVAL, Constant.CON_RETRY_INTERVAL)
            prefManager.setPref(Constant.PREF_FREE_CONVERT, Constant.CON_FREE_CONVERT)
            prefManager.setPref(Constant.PREF_FREE_CONVERT_COUNT, Constant.CON_FREE_CONVERT_COUNT)
            prefManager.setPref(Constant.PREF_FREE_CONVERT_EVERY_X, Constant.CON_FREE_CONVERT_EVERY_X)
            prefManager.setPref(Constant.PREF_FREE_CONVERT_BELOW_X_EUR, Constant.CON_FREE_CONVERT_BELOW_X_EUR)
            prefManager.setPref(Constant.PREF_REFRESH_REGULARLY, Constant.CON_REFRESH_REGULARLY)
            prefManager.setPref(Constant.PREF_REFRESH_INTERVAL, Constant.CON_REFRESH_INTERVAL)
            prefManager.setPref(Constant.PREF_HOME_BALANCE_COUNT, Constant.CON_HOME_BALANCE_COUNT)
            prefManager.setPref(Constant.PREF_INITIAL_BALANCE_VALUE, Constant.CON_INITIAL_BALANCE_VALUE)
            prefManager.setPref(Constant.PREF_INITIAL_BALANCE_NAME, Constant.CON_INITIAL_BALANCE_NAME)
            prefManager.setPref(Constant.PREF_CONVERSION_FEE, Constant.CON_CONVERSION_FEE)
            prefManager.setPref(Constant.PREF_REDUCE_FEE_FROM_SOURCE, Constant.CON_REDUCE_FEE_FROM_SOURCE)
            prefManager.setPref(Constant.PREF_REDUCE_FEE_FROM_TARGET, Constant.CON_REDUCE_FEE_FROM_TARGET)
            prefManager.setPref(Constant.PREF_INITIALIZED, true)
        }
        retryInterval = prefManager.getIntPref(Constant.PREF_RETRY_INTERVAL)
        freeConvert = prefManager.getBooleanPref(Constant.PREF_FREE_CONVERT)
        freeConvertCount = prefManager.getIntPref(Constant.PREF_FREE_CONVERT_COUNT)
        freeConvertEveryX = prefManager.getIntPref(Constant.PREF_FREE_CONVERT_EVERY_X)
        freeConvertBelowXEur = prefManager.getDoublePref(Constant.PREF_FREE_CONVERT_BELOW_X_EUR)
        refreshRegularly = prefManager.getBooleanPref(Constant.PREF_REFRESH_REGULARLY)
        refreshInterval = prefManager.getIntPref(Constant.PREF_REFRESH_INTERVAL)
        homeBalanceCount = prefManager.getIntPref(Constant.PREF_HOME_BALANCE_COUNT)
        initialBalanceValue = prefManager.getDoublePref(Constant.PREF_INITIAL_BALANCE_VALUE)
        initialBalanceName = prefManager.getStringPref(Constant.PREF_INITIAL_BALANCE_NAME) ?: Constant.CON_INITIAL_BALANCE_NAME
        conversionFee = prefManager.getDoublePref(Constant.PREF_CONVERSION_FEE)
        reduceFeeFromSource = prefManager.getBooleanPref(Constant.PREF_REDUCE_FEE_FROM_SOURCE)
        reduceFeeFromTarget = prefManager.getBooleanPref(Constant.PREF_REDUCE_FEE_FROM_TARGET)
    }
}