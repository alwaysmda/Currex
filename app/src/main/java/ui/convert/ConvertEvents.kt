package ui.convert

import domain.model.Rate
import ui.base.BaseEvent
import util.StringResource

sealed class ConvertEvents : BaseEvent() {
    class UpdateBalanceList(val list: ArrayList<Rate>) : ConvertEvents()
    class NavCurrencyList(val list: ArrayList<Rate>, val sellRate: Rate, val receiveRate: Rate) : ConvertEvents()
    class NavBalanceList(val list: ArrayList<Rate>) : ConvertEvents()
    object NavSetting : ConvertEvents()
    class UpdateSellText(val text: String) : ConvertEvents()
    class UpdateReceiveText(val text: String) : ConvertEvents()
    class ShowConvertCompleteDialog(val content: StringResource, val fee: StringResource) : ConvertEvents()
}