package ui.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.usecase.template.TemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import javax.inject.Inject

@HiltViewModel
class TemplateBindingViewModel @Inject constructor(
    val app: ApplicationClass,
    private val templateUseCases: TemplateUseCases,
) : ViewModel(), TemplateAction {

    val action: TemplateAction = this

    var buttonText = MutableStateFlow(app.m.appName)

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
        buttonText.value = app.m.title
    }

    private fun doTemplate() = viewModelScope.launch {
        templateUseCases.template(123).onEach {
            when (it) {
                is DataState.Loading -> {
                    buttonText.value = app.m.loading
                }
                is DataState.Success -> {
                    buttonText.value = app.m.appName
                }
                is DataState.Failure -> {
                    buttonText.value = app.m.nothingToShow
                }
            }
        }.launchIn(viewModelScope)
    }
}




