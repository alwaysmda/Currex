package ui.setting

import domain.model.Option
import ui.base.BaseEvent
import util.StringResource

sealed class SettingEvents : BaseEvent() {
    class Snack(val message: StringResource) : SettingEvents()
    class UpdateRetryIntervalList(val list: ArrayList<Option<Int>>) : SettingEvents()
    class UpdateFreeConvertCountList(val list: ArrayList<Option<Int>>) : SettingEvents()
    class UpdateFreeConvertEveryXList(val list: ArrayList<Option<Int>>) : SettingEvents()
    class UpdateFreeConvertBelowXEurList(val list: ArrayList<Option<Double>>) : SettingEvents()
    class UpdateRefreshRegularlyList(val list: ArrayList<Option<Boolean>>) : SettingEvents()
    class UpdateRefreshIntervalList(val list: ArrayList<Option<Int>>) : SettingEvents()
    class UpdateHomeBalanceCountList(val list: ArrayList<Option<Int>>) : SettingEvents()
    class UpdateInitialBalanceValueList(val list: ArrayList<Option<Double>>) : SettingEvents()
    class UpdateInitialBalanceNameList(val list: ArrayList<Option<String>>) : SettingEvents()
    class UpdateConversionFeeList(val list: ArrayList<Option<Double>>) : SettingEvents()
    class UpdateReduceFeeFromSourceList(val list: ArrayList<Option<Boolean>>) : SettingEvents()
    class UpdateReduceFeeFromTargetList(val list: ArrayList<Option<Boolean>>) : SettingEvents()
    object NavBack : SettingEvents()
    class SendMail(val email: String) : SettingEvents()
}