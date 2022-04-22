package ui.convert

import domain.model.Rate
import main.ApplicationClass
import ui.base.BaseEvent
import ui.dialog.CustomDialog

sealed class ConvertEvents : BaseEvent() {
    class Rebind(val app: ApplicationClass) : ConvertEvents()
    class Snack(val message: String) : ConvertEvents()
    class UpdateBalanceList(val list: ArrayList<Rate>) : ConvertEvents()
    class NavCurrencyList(val list: ArrayList<Rate>, val sellRate: Rate, val receiveRate: Rate) : ConvertEvents()
    class NavBalanceList(val list: ArrayList<Rate>) : ConvertEvents()
    object NavSetting : ConvertEvents()
    class UpdateSellText(val text: String) : ConvertEvents()
    class UpdateReceiveText(val text: String) : ConvertEvents()
    class ShowDialog(val dialog: CustomDialog) : ConvertEvents()
}