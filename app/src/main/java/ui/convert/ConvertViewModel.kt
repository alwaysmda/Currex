package ui.convert

import androidx.lifecycle.viewModelScope
import com.example.currex.R
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.model.Rate
import domain.usecase.convert.ConvertUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import ui.base.BaseViewModel
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
    private lateinit var retryTimer: TimerTask

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

    init {
        retryTimer = object : TimerTask() {
            override fun run() {
                if (retryTimerCounter == 0) {
                    retryTimer.cancel()
                    retryTimerCounter = Constant.CON_RETRY_DELAY
                    isActive = true
                    getRatesJob = getRates()
                } else {
                    errorDescText.value = app.resources.getQuantityString(R.plurals.retrying_in_x_sec, retryTimerCounter)
                }
                retryTimerCounter--
            }
        }
    }

    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            getRatesJob = getRates()
        } else {
            viewModelScope.launch {
                _event.emit(ConvertEvents.UpdateBalanceList(ArrayList(balanceList.subList(0, 10))))
            }
        }
    }

    override fun onSellTextChanged(text: String) {
        val sellRate = rateList.first { item -> item.name == sellCurrencyText.value }.value
        val receiveRate = rateList.first { item -> item.name == receiveCurrencyText.value }.value
        val result = convertUseCases.convertRateUseCase(text, sellRate, receiveRate)
        viewModelScope.launch {
            _event.emit(ConvertEvents.UpdateReceiveText(result))
        }
    }

    override fun onReceiveTextChanged(text: String) {
        val sellRate = rateList.first { item -> item.name == sellCurrencyText.value }.value
        val receiveRate = rateList.first { item -> item.name == receiveCurrencyText.value }.value
        val result = convertUseCases.convertRateUseCase(text, receiveRate, sellRate)
        viewModelScope.launch {
            _event.emit(ConvertEvents.UpdateSellText(result))
        }
    }

    private fun getRates(): Job {
        return viewModelScope.launch {
//            while (isActive) {
            convertUseCases.getExchangeRateUseCase().onEach {
                when (it) {
                    is DataState.Loading -> onGetRatesLoading()
                    is DataState.Failure -> onGetRatesFailure(it)
                    is DataState.Success -> onGetRatesSuccess(it)
                }
            }.launchIn(viewModelScope)
            delay(Constant.CON_REFRESH_DELAY_MILLIS)
//            }
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

    }

    override fun onBalanceMoreClick() {
        viewModelScope.launch {
            _event.emit(ConvertEvents.NavBalanceList(balanceList))
        }
    }

    private fun onGetRatesLoading() {
        loadingVisibility.value = rateList.isEmpty()
        failVisibility.value = false
        contentVisibility.value = false
    }

    private fun onGetRatesFailure(error: DataState.Failure) {
        loadingVisibility.value = false
        failVisibility.value = rateList.isEmpty()
        errorVisibility.value = rateList.isNotEmpty()
        contentVisibility.value = false
        isActive = false
        errorTitleText.value = error.message
        Timer().scheduleAtFixedRate(retryTimer, 0, 1000)
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
            var balance = convertUseCases.addMissingBalanceUseCase(result.data)
            sellCurrencyText.value = app.prefManager.getStringPref(Constant.PREF_SELL) ?: if (rateList.any { item -> item.name == "EUR" }) "EUR" else rateList[0].name
            receiveCurrencyText.value = app.prefManager.getStringPref(Constant.PREF_RECEIVE) ?: if (rateList.any { item -> item.name == "USD" }) "USD" else rateList[1].name
            balance = convertUseCases.sortBalanceUseCase(balance, rateList.first { it.name == sellCurrencyText.value }, rateList.first { it.name == receiveCurrencyText.value })
            balanceList.clear()
            balanceList.addAll(balance)
            viewModelScope.launch {
                _event.emit(ConvertEvents.UpdateBalanceList(ArrayList(balanceList.subList(0, 10))))
            }
        }
    }
}




