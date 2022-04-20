package ui.convert

import main.ApplicationClass
import ui.base.BaseEvent

sealed class ConvertEvents : BaseEvent() {
    class Rebind(val app: ApplicationClass) : ConvertEvents()
    class Snack(val message: String) : ConvertEvents()
}