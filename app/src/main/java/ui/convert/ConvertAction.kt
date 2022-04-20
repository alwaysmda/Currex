package ui.convert

import ui.base.BaseAction

interface ConvertAction : BaseAction {
    fun onStart()
    fun onButtonClick()
}