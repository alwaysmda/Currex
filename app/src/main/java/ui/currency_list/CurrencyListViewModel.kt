package ui.currency_list

import androidx.lifecycle.viewModelScope
import com.example.currex.R
import dagger.hilt.android.lifecycle.HiltViewModel
import domain.model.Rate
import domain.model.Rate.Companion.cloned
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    app: ApplicationClass,
) : BaseViewModel<CurrencyListEvents, CurrencyListAction>(app), CurrencyListAction {
    //Local
    private var balanceList = arrayListOf<Rate>()
    private lateinit var sellRate: Rate
    private lateinit var receiveRate: Rate

    //Binding
    var searchText = MutableStateFlow("")
    var clearTextVisibility = MutableStateFlow(false)
    var failVisibility = MutableStateFlow(false)
    var errorTitleText = MutableStateFlow(app.getString(R.string.no_result))
    var errorDescText = MutableStateFlow(app.getString(R.string.no_currency_found))

    init {
        searchText.onEach { text ->
            if (text.isBlank()) {
                clearTextVisibility.value = false
                failVisibility.value = false
                _event.emit(CurrencyListEvents.UpdateCurrencyList(balanceList.cloned()))
            } else {
                clearTextVisibility.value = true
                val result = balanceList.filter { it.name.lowercase().contains(text.lowercase()) }.cloned()
                if (result.isEmpty()) {
                    failVisibility.value = true
                } else {
                    failVisibility.value = false
                    _event.emit(CurrencyListEvents.UpdateCurrencyList(result))
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onStart(list: ArrayList<Rate>, sellRate: Rate, receiveRate: Rate) {
        if (isFirstStart) {
            isFirstStart = false
            balanceList.clear()
            balanceList.addAll(list)
            this.sellRate = sellRate
            this.receiveRate = receiveRate
            balanceList.first { it.name == sellRate.name }.apply {
                isSell = true
                selected = true
            }
            balanceList.first { it.name == receiveRate.name }.apply {
                isReceive = true
                selected = true
            }
            viewModelScope.launch {
                _event.emit(CurrencyListEvents.UpdateCurrencyList(balanceList.cloned()))
            }
        } else {
            //return
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _event.emit(CurrencyListEvents.NavBack)
        }
    }

    override fun onClearTextClick() {
        searchText.value = ""
    }

    override fun onCurrencyClick(index: Int, rate: Rate) {
        viewModelScope.launch {
            if (rate.selected) {
                _event.emit(CurrencyListEvents.Snack(app.getString(R.string.this_currency_is_already_selected)))
            } else {
                _event.emit(CurrencyListEvents.ReturnCurrency(rate))
            }
        }
    }

}




