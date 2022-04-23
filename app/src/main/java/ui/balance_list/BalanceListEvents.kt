package ui.balance_list

import domain.model.Rate
import ui.base.BaseEvent

sealed class BalanceListEvents : BaseEvent() {
    class UpdateBalanceList(val list: ArrayList<Rate>) : BalanceListEvents()
    object NavBack : BalanceListEvents()
}