package ui.template

import main.ApplicationClass

sealed class TemplateMVIEvents {
    class Rebind(val app: ApplicationClass) : TemplateMVIEvents()
    class Snack(val message: String) : TemplateMVIEvents()
}