package ui.balance_list

import domain.model.Rate
import ui.base.BaseAction

interface BalanceListAction : BaseAction {
    fun onStart(list: ArrayList<Rate>)
    fun onBackClick()
    fun onClearTextClick()
}