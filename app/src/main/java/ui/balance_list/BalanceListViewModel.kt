package ui.balance_list

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
import util.StringResource
import javax.inject.Inject

@HiltViewModel
class BalanceListViewModel @Inject constructor(
    app: ApplicationClass,
) : BaseViewModel<BalanceListEvents, BalanceListAction>(app), BalanceListAction {
    //Local
    private var balanceList = arrayListOf<Rate>()

    //Binding
    var searchText = MutableStateFlow("")
    var clearTextVisibility = MutableStateFlow(false)
    var failVisibility = MutableStateFlow(false)
    var errorTitleText = MutableStateFlow(StringResource.Translatable(R.string.no_result))
    var errorDescText = MutableStateFlow(StringResource.Translatable(R.string.no_currency_found))

    init {
        searchText.onEach { text ->
            if (text.isBlank()) {
                clearTextVisibility.value = false
                failVisibility.value = false
                _event.emit(BalanceListEvents.UpdateBalanceList(balanceList.cloned()))
            } else {
                clearTextVisibility.value = true
                val result = balanceList.filter { it.name.lowercase().contains(text.lowercase()) }.cloned()
                if (result.isEmpty()) {
                    failVisibility.value = true
                } else {
                    failVisibility.value = false
                    _event.emit(BalanceListEvents.UpdateBalanceList(result))
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onStart(list: ArrayList<Rate>) {
        if (isFirstStart) {
            isFirstStart = false
            balanceList.clear()
            balanceList.addAll(list)
            viewModelScope.launch {
                _event.emit(BalanceListEvents.UpdateBalanceList(balanceList.cloned()))
            }
        } else {
            //return
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _event.emit(BalanceListEvents.NavBack)
        }
    }

    override fun onClearTextClick() {
        searchText.value = ""
    }

}




