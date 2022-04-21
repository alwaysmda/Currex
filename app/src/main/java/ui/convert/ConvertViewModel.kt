package ui.convert

import androidx.lifecycle.viewModelScope
import com.example.currex.R
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.model.ConvertResult
import domain.model.Rate
import domain.model.Rate.Companion.cloned
import domain.usecase.convert.ConvertUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import ui.base.BaseViewModel
import ui.dialog.CustomDialog
import util.Constant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    app: ApplicationClass,
    private val convertUseCases: ConvertUseCases,
) : BaseViewModel<ConvertEvents, ConvertAction>(app), ConvertAction {
    //Local
    private var getRatesJob: Job? = null
    private var isActive = true
    private var balanceList = arrayListOf<Rate>()
    private var rateList = arrayListOf<Rate>()
    private var retryTimerCounter = Constant.CON_RETRY_DELAY
    private var sellRate = Rate("", 0.0)
    private var receiveRate = Rate("", 0.0)
    private var lastConvertResult = ConvertResult(0.0, 0.0, 0.0, 0.0, false, "", "")

    //Binding
    var loadingVisibility = MutableStateFlow(true)
    var failVisibility = MutableStateFlow(false)
    var errorVisibility = MutableStateFlow(false)
    var errorTitleText = MutableStateFlow("")
    var errorDescText = MutableStateFlow("")
    var contentVisibility = MutableStateFlow(false)
    var sellCurrencyText = MutableStateFlow("")
    var receiveCurrencyText = MutableStateFlow("")
    var validationErrorText = MutableStateFlow("")
    var validationErrorTextVisibility = MutableStateFlow(false)
    var convertButtonEnabled = MutableStateFlow(false)
    var freeConvertText = MutableStateFlow("")


    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            getRatesJob = getRates()
        } else {
            viewModelScope.launch {
                _event.emit(ConvertEvents.UpdateBalanceList(balanceList.subList(0, Constant.CON_HOME_BALANCE_COUNT).cloned()))
            }
        }
        updateFreeConversionText()
    }

    override fun onSellTextChanged(text: String) {
        val result = convertUseCases.convertRateUseCase(text, sellRate.value, receiveRate.value, true, balanceList)
        updateConvertResult(result)
        viewModelScope.launch {
            _event.emit(ConvertEvents.UpdateReceiveText(result.receiveString))
        }
    }

    override fun onReceiveTextChanged(text: String) {
        val result = convertUseCases.convertRateUseCase(text, receiveRate.value, sellRate.value, false, balanceList)
        updateConvertResult(result)
        viewModelScope.launch {
            _event.emit(ConvertEvents.UpdateSellText(result.sellString))
        }
    }

    override fun onCleared() {
        getRatesJob?.cancel()
        super.onCleared()
    }

    override fun onErrorDismissClick() {
        errorVisibility.value = false
    }

    override fun onSellCurrencyClick() {

    }

    override fun onReceiveCurrencyClick() {

    }

    override fun onConvertClick() {
        val balance = convertUseCases.applyConvertUseCase(lastConvertResult, balanceList.cloned())
        balanceList.clear()
        balanceList.addAll(balance)
        var message = app.getString(
            R.string.convert_success_desc,
            lastConvertResult.sellString,
            sellRate.name,
            lastConvertResult.receiveString,
            receiveRate.name
        )

        if (lastConvertResult.sellFee > 0.0) {
            message += "\n"
            message += app.getString(R.string.convert_fee_desc, lastConvertResult.sellFeeString, sellRate.name)
        }
        viewModelScope.launch {
            _event.emit(ConvertEvents.UpdateBalanceList(balanceList.subList(0, Constant.CON_HOME_BALANCE_COUNT).cloned()))
            _event.emit(
                ConvertEvents.ShowDialog(
                    CustomDialog(app)
                        .setTitle(app.getString(R.string.convert_success_title))
                        .setContent(message)
                        .setPositiveText(app.getString(R.string.confirm))
                )
            )
        }
        updateFreeConversionText()
    }

    override fun onBalanceMoreClick() {
        viewModelScope.launch {
            _event.emit(ConvertEvents.NavBalanceList(balanceList))
        }
    }

    private fun getRates(): Job {
        return viewModelScope.launch {
//            while (isActive) {
            convertUseCases.getExchangeRateUseCase().onEach {
                if (isActive) {
                    when (it) {
                        is DataState.Loading -> onGetRatesLoading()
                        is DataState.Failure -> onGetRatesFailure(it)
                        is DataState.Success -> onGetRatesSuccess(it)
                    }
                }
            }.launchIn(viewModelScope)
            delay(Constant.CON_REFRESH_DELAY_MILLIS)
//            }
        }
    }

    private fun onGetRatesLoading() {
        loadingVisibility.value = rateList.isEmpty()
        failVisibility.value = false
        contentVisibility.value = rateList.isNotEmpty()
    }

    private fun onGetRatesFailure(error: DataState.Failure) {
        loadingVisibility.value = false
        failVisibility.value = rateList.isEmpty()
        errorVisibility.value = rateList.isNotEmpty()
        contentVisibility.value = rateList.isNotEmpty()
        isActive = false
        errorTitleText.value = error.message
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (retryTimerCounter == 0) {
                    cancel()
                    retryTimerCounter = Constant.CON_RETRY_DELAY
                    isActive = true
                    getRatesJob = getRates()
                } else {
                    errorDescText.value = app.resources.getQuantityString(R.plurals.retrying_in_x_sec, retryTimerCounter, retryTimerCounter)
                    retryTimerCounter--
                }
            }
        }, 0, 1000)
    }

    private fun onGetRatesSuccess(result: DataState.Success<ArrayList<Rate>>) {
        if (result.data.size < 2) {
            onGetRatesFailure(DataState.Failure(DataState.Failure.CODE_INVALID, app.getString(R.string.something_went_wrong)))
        } else {
            loadingVisibility.value = false
            failVisibility.value = false
            errorVisibility.value = false
            contentVisibility.value = true
            rateList.clear()
            rateList.addAll(result.data)
            if (balanceList.isEmpty()) {
                var balance = convertUseCases.addMissingBalanceUseCase(result.data)
                val savedSellCurrency = app.prefManager.getStringPref(Constant.PREF_SELL) ?: "EUR"
                val savedReceiveCurrency = app.prefManager.getStringPref(Constant.PREF_RECEIVE) ?: "USD"

                sellRate = rateList.firstOrNull { it.name == savedSellCurrency } ?: rateList[0]
                receiveRate = rateList.firstOrNull { it.name == savedReceiveCurrency } ?: rateList[1]

                sellCurrencyText.value = sellRate.name
                receiveCurrencyText.value = receiveRate.name

                balance = convertUseCases.sortBalanceUseCase(balance, sellRate, receiveRate)
                balanceList.addAll(balance)
                viewModelScope.launch {
                    _event.emit(ConvertEvents.UpdateBalanceList(balanceList.subList(0, Constant.CON_HOME_BALANCE_COUNT).cloned()))
                }
            }
            onSellTextChanged(lastConvertResult.sellString)
        }
    }

    private fun updateFreeConversionText() {
        val remainingFreeConvertCount = app.prefManager.getIntPref(Constant.PREF_FREE_CONVERT_COUNT)
        freeConvertText.value = app.resources.getQuantityString(R.plurals.x_free_conversion_left, remainingFreeConvertCount, remainingFreeConvertCount)
    }

    private fun updateConvertResult(result: ConvertResult) {
        lastConvertResult = result
        convertButtonEnabled.value = result.isValid
        validationErrorText.value = result.error
        validationErrorTextVisibility.value = result.error.isNotBlank()
    }
}




