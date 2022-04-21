package ui.currency_list

import domain.model.Rate
import ui.base.BaseAction

interface CurrencyListAction : BaseAction {
    fun onStart(list: ArrayList<Rate>, sellRate: Rate, receiveRate: Rate)
    fun onBackClick()
    fun onClearTextClick()
    fun onCurrencyClick(index: Int, rate: Rate)
}