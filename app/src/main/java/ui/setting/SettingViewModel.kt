package ui.setting

import androidx.lifecycle.viewModelScope
import com.example.currex.R
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.model.Option
import domain.model.Option.Companion.cloned
import domain.usecase.convert.ConvertUseCases
import kotlinx.coroutines.launch
import main.ApplicationClass
import ui.base.BaseViewModel
import util.Constant
import util.StringResource
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    app: ApplicationClass,
    private val convertUseCases: ConvertUseCases
) : BaseViewModel<SettingEvents, SettingAction>(app), SettingAction {
    //Local
    private var retryIntervalList = arrayListOf(
        Option("3", 3),
        Option("5", 5),
        Option("10", 10),
        Option("15", 15),
        Option("30", 30),
        Option("60", 60),
    )
    private var freeConvertCountList = arrayListOf(
        Option("None", 0),
        Option("1", 1),
        Option("5", 5),
        Option("10", 10),
        Option("20", 20),
        Option("30", 30),
        Option("All", -1),
    )
    private var freeConvertEveryXList = arrayListOf(
        Option("None", 0),
        Option("5", 5),
        Option("10", 10),
        Option("20", 20),
        Option("30", 30),
    )
    private var freeConvertBelowXEurList = arrayListOf(
        Option("None", 0.0),
        Option("20", 20.0),
        Option("50", 50.0),
        Option("100", 100.0),
        Option("200", 200.0),
        Option("500", 500.0),
        Option("1,000", 1000.0),
        Option("5,000", 5000.0),
        Option("10,000", 10000.0),
    )
    private var refreshRegularlyList = arrayListOf(
        Option("True", true),
        Option("False", false),
    )
    private var refreshIntervalList = arrayListOf(
        Option("3", 3),
        Option("5", 5),
        Option("10", 10),
        Option("15", 15),
        Option("30", 30),
        Option("60", 60),
    )
    private var homeBalanceCountList = arrayListOf(
        Option("2", 2),
        Option("5", 5),
        Option("10", 10),
        Option("15", 15),
        Option("30", 30),
        Option("60", 60),
        Option("All", Int.MAX_VALUE),
    )
    private var initialBalanceValueList = arrayListOf(
        Option("Zero", 0.0),
        Option("1", 1.0),
        Option("5", 5.0),
        Option("10", 10.0),
        Option("20", 20.0),
        Option("50", 50.0),
        Option("100", 100.0),
        Option("200", 200.0),
        Option("500", 500.0),
        Option("1,000", 1000.0),
        Option("5,000", 5000.0),
        Option("10,000", 10000.0),
    )
    private var initialBalanceNameList = arrayListOf(
        Option("USD", "USD"),
        Option("EUR", "EUR"),
        Option("JPY", "JPY"),
        Option("GBP", "GBP"),
        Option("AUD", "AUD"),
        Option("CAD", "CAD"),
        Option("CHF", "CHF"),
        Option("CNY", "CNY"),
        Option("HKD", "HKD"),
        Option("NZD", "NZD"),
        Option("SEK", "SEK"),
        Option("KRW", "KRW"),
    )
    private var conversionFeeList = arrayListOf(
        Option("Free", 0.0),
        Option("0.1%", 0.001),
        Option("0.3%", 0.003),
        Option("0.7%", 0.007),
        Option("1%", 0.01),
        Option("1.5%", 0.015),
        Option("2%", 0.02),
        Option("5%", 0.05),
    )
    private var reduceFeeFromSourceList = arrayListOf(
        Option("True", true),
        Option("False", false),
    )
    private var reduceFeeFromTargetList = arrayListOf(
        Option("True", true),
        Option("False", false),
    )


    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            updateAllLists()
        } else {
            //return
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _event.emit(SettingEvents.NavBack)
        }
    }

    override fun onRetryIntervalClick(index: Int, option: Option<Int>) {
        app.appSetting.retryInterval = option.value
        retryIntervalList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateRetryIntervalList(retryIntervalList.cloned()))
        }
    }

    override fun onFreeConvertCountClick(index: Int, option: Option<Int>) {
        if (option.value == -1) {
            app.appSetting.freeConvert = true
            app.appSetting.freeConvertCount = 0
        } else {
            app.appSetting.freeConvert = false
            app.appSetting.freeConvertCount = option.value
        }
        freeConvertCountList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateFreeConvertCountList(freeConvertCountList.cloned()))
        }
    }

    override fun onFreeConvertEveryXClick(index: Int, option: Option<Int>) {
        app.appSetting.freeConvertEveryX = option.value
        freeConvertEveryXList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateFreeConvertEveryXList(freeConvertEveryXList.cloned()))
        }
    }

    override fun onFreeConvertBelowXEurClick(index: Int, option: Option<Double>) {
        app.appSetting.freeConvertBelowXEur = option.value
        freeConvertBelowXEurList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateFreeConvertBelowXEurList(freeConvertBelowXEurList.cloned()))
        }
    }

    override fun onRefreshRegularlyClick(index: Int, option: Option<Boolean>) {
        app.appSetting.refreshRegularly = option.value
        refreshRegularlyList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateRefreshRegularlyList(refreshRegularlyList.cloned()))
        }
    }

    override fun onRefreshIntervalClick(index: Int, option: Option<Int>) {
        app.appSetting.refreshInterval = option.value
        refreshIntervalList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateRefreshIntervalList(refreshIntervalList.cloned()))
        }
    }

    override fun onHomeBalanceCountClick(index: Int, option: Option<Int>) {
        app.appSetting.homeBalanceCount = option.value
        homeBalanceCountList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateHomeBalanceCountList(homeBalanceCountList.cloned()))
        }
    }

    override fun onInitialBalanceValueClick(index: Int, option: Option<Double>) {
        app.appSetting.initialBalanceValue = option.value
        initialBalanceValueList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateInitialBalanceValueList(initialBalanceValueList.cloned()))
        }
    }

    override fun onInitialBalanceNameClick(index: Int, option: Option<String>) {
        app.appSetting.initialBalanceName = option.value
        initialBalanceNameList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateInitialBalanceNameList(initialBalanceNameList.cloned()))
        }
    }

    override fun onConversionFeeClick(index: Int, option: Option<Double>) {
        app.appSetting.conversionFee = option.value
        conversionFeeList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateConversionFeeList(conversionFeeList.cloned()))
        }
    }

    override fun onReduceFeeFromSourceClick(index: Int, option: Option<Boolean>) {
        app.appSetting.reduceFeeFromSource = option.value
        reduceFeeFromSourceList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateReduceFeeFromSourceList(reduceFeeFromSourceList.cloned()))
        }
    }

    override fun onReduceFeeFromTargetClick(index: Int, option: Option<Boolean>) {
        app.appSetting.reduceFeeFromTarget = option.value
        reduceFeeFromTargetList.forEach { it.selected = it.value == option.value }
        viewModelScope.launch {
            _event.emit(SettingEvents.UpdateReduceFeeFromTargetList(reduceFeeFromTargetList.cloned()))
        }
    }


    override fun onResetBalanceClick() {
        viewModelScope.launch {
            convertUseCases.resetBalanceListUseCase()
            _event.emit(SettingEvents.Snack(StringResource.Translatable(R.string.done)))
        }
    }

    override fun onResetOptionsClick() {
        app.prefManager.setPref(Constant.PREF_INITIALIZED, false)
        app.appSetting.init()
        updateAllLists()
        viewModelScope.launch {
            _event.emit(SettingEvents.Snack(StringResource.Translatable(R.string.done)))
        }
    }

    override fun onContactDeveloperClick() {
        viewModelScope.launch {
            _event.emit(SettingEvents.SendMail(Constant.CON_DEVELOPER_EMAIL))
        }
    }

    private fun updateAllLists() {
        viewModelScope.launch {
            retryIntervalList.forEach { it.selected = it.value == app.appSetting.retryInterval }
            if (app.appSetting.freeConvert) {
                freeConvertCountList.last().selected = true
            } else {
                freeConvertCountList.forEach { it.selected = it.value == app.appSetting.freeConvertCount }
            }
            freeConvertEveryXList.forEach { it.selected = it.value == app.appSetting.freeConvertEveryX }
            freeConvertBelowXEurList.forEach { it.selected = it.value == app.appSetting.freeConvertBelowXEur }
            refreshRegularlyList.forEach { it.selected = it.value == app.appSetting.refreshRegularly }
            refreshIntervalList.forEach { it.selected = it.value == app.appSetting.refreshInterval }
            homeBalanceCountList.forEach { it.selected = it.value == app.appSetting.homeBalanceCount }
            if (homeBalanceCountList.none { it.selected }) {
                homeBalanceCountList.last().selected = true
            }
            initialBalanceValueList.forEach { it.selected = it.value == app.appSetting.initialBalanceValue }
            initialBalanceNameList.forEach { it.selected = it.value == app.appSetting.initialBalanceName }
            conversionFeeList.forEach { it.selected = it.value == app.appSetting.conversionFee }
            reduceFeeFromSourceList.forEach { it.selected = it.value == app.appSetting.reduceFeeFromSource }
            reduceFeeFromTargetList.forEach { it.selected = it.value == app.appSetting.reduceFeeFromTarget }



            _event.emit(SettingEvents.UpdateRetryIntervalList(retryIntervalList.cloned()))
            _event.emit(SettingEvents.UpdateFreeConvertCountList(freeConvertCountList.cloned()))
            _event.emit(SettingEvents.UpdateFreeConvertEveryXList(freeConvertEveryXList.cloned()))
            _event.emit(SettingEvents.UpdateFreeConvertBelowXEurList(freeConvertBelowXEurList.cloned()))
            _event.emit(SettingEvents.UpdateRefreshRegularlyList(refreshRegularlyList.cloned()))
            _event.emit(SettingEvents.UpdateRefreshIntervalList(refreshIntervalList.cloned()))
            _event.emit(SettingEvents.UpdateHomeBalanceCountList(homeBalanceCountList.cloned()))
            _event.emit(SettingEvents.UpdateInitialBalanceValueList(initialBalanceValueList.cloned()))
            _event.emit(SettingEvents.UpdateInitialBalanceNameList(initialBalanceNameList.cloned()))
            _event.emit(SettingEvents.UpdateConversionFeeList(conversionFeeList.cloned()))
            _event.emit(SettingEvents.UpdateReduceFeeFromSourceList(reduceFeeFromSourceList.cloned()))
            _event.emit(SettingEvents.UpdateReduceFeeFromTargetList(reduceFeeFromTargetList.cloned()))
        }
    }
}




