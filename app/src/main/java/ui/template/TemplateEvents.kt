package ui.template

import main.ApplicationClass

sealed class TemplateEvents {
    class Rebind(val app: ApplicationClass) : TemplateEvents()
    class Snack(val message: String) : TemplateEvents()
}