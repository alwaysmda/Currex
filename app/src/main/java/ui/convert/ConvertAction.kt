package ui.convert

import domain.model.Rate
import ui.base.BaseAction

interface ConvertAction : BaseAction {
    fun onStart()
    fun onErrorDismissClick()
    fun onSellCurrencyClick()
    fun onReceiveCurrencyClick()
    fun onSellTextChanged(text: String)
    fun onReceiveTextChanged(text: String)
    fun onConvertClick()
    fun onBalanceMoreClick()
    fun onCurrencyChanged(rate: Rate)
}