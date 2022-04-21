package ui.convert

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
}