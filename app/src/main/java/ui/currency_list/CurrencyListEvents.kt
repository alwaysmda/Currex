package ui.currency_list

import domain.model.Rate
import main.ApplicationClass
import ui.base.BaseEvent

sealed class CurrencyListEvents : BaseEvent() {
    class Rebind(val app: ApplicationClass) : CurrencyListEvents()
    class Snack(val message: String) : CurrencyListEvents()
    class UpdateCurrencyList(val list: ArrayList<Rate>) : CurrencyListEvents()
    object NavBack : CurrencyListEvents()
    class ReturnCurrency(val rate: Rate) : CurrencyListEvents()
}