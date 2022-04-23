package ui.currency_list

import domain.model.Rate
import ui.base.BaseEvent
import util.StringResource

sealed class CurrencyListEvents : BaseEvent() {
    class Snack(val message: StringResource) : CurrencyListEvents()
    class UpdateCurrencyList(val list: ArrayList<Rate>) : CurrencyListEvents()
    object NavBack : CurrencyListEvents()
    class ReturnCurrency(val rate: Rate) : CurrencyListEvents()
}