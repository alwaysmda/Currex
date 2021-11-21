package ui.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.usecase.template.TemplateUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import javax.inject.Inject

@HiltViewModel
class TemplateMVIViewModel @Inject constructor(
    val app: ApplicationClass,
    private val templateUseCases: TemplateUseCases,
) : ViewModel(), TemplateAction {

    val action: TemplateAction = this

    private var _state = MutableStateFlow<TemplateMVIState>(TemplateMVIState.Idle)
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<TemplateEvents>()
    val event = _event.asSharedFlow()

    private var isFirstStart = true


    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            doTemplate()
        } else {
            //return
        }
    }

    override fun onButtonClick() {
        viewModelScope.launch {
            _event.emit(TemplateEvents.Snack(app.m.title))
        }
    }

    private fun doTemplate() = viewModelScope.launch {
        templateUseCases.template(123).onEach {
            when (it) {
                is DataState.Loading -> {
                    _state.emit(TemplateMVIState.Loading)
                }
                is DataState.Success -> {
                    _state.emit(TemplateMVIState.Update(app.m.appName))
                }
                is DataState.Failure -> {
                    _state.emit(TemplateMVIState.Update(app.m.nothingToShow))
                }
            }
        }.launchIn(viewModelScope)
    }
}




