package ui.balance_list

import domain.model.Rate
import main.ApplicationClass
import ui.base.BaseEvent

sealed class BalanceListEvents : BaseEvent() {
    class Rebind(val app: ApplicationClass) : BalanceListEvents()
    class Snack(val message: String) : BalanceListEvents()
    class UpdateBalanceList(val list: ArrayList<Rate>) : BalanceListEvents()
    object NavBack : BalanceListEvents()
}