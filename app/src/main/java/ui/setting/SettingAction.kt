package ui.setting

import domain.model.Option
import ui.base.BaseAction

interface SettingAction : BaseAction {
    fun onStart()
    fun onBackClick()
    fun onResetBalanceClick()
    fun onResetOptionsClick()
    fun onContactDeveloperClick()
    fun onRetryIntervalClick(index: Int, option: Option<Int>)
    fun onFreeConvertCountClick(index: Int, option: Option<Int>)
    fun onFreeConvertEveryXClick(index: Int, option: Option<Int>)
    fun onFreeConvertBelowXEurClick(index: Int, option: Option<Double>)
    fun onRefreshRegularlyClick(index: Int, option: Option<Boolean>)
    fun onRefreshIntervalClick(index: Int, option: Option<Int>)
    fun onHomeBalanceCountClick(index: Int, option: Option<Int>)
    fun onInitialBalanceValueClick(index: Int, option: Option<Double>)
    fun onInitialBalanceNameClick(index: Int, option: Option<String>)
    fun onConversionFeeClick(index: Int, option: Option<Double>)
    fun onReduceFeeFromSourceClick(index: Int, option: Option<Boolean>)
    fun onReduceFeeFromTargetClick(index: Int, option: Option<Boolean>)

}