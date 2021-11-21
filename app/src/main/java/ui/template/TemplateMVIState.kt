package ui.template

sealed class TemplateMVIState {
    object Idle : TemplateMVIState()
    object Loading : TemplateMVIState()
    class Update(val data: String) : TemplateMVIState()
}

