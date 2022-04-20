package ui.convert

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.usecase.convert.ConvertUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import ui.base.BaseState
import ui.base.BaseViewModel
import util.extension.log
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    app: ApplicationClass,
    private val convertUseCases: ConvertUseCases,
) : BaseViewModel<ConvertEvents, ConvertAction, BaseState>(app), ConvertAction {
    var buttonText = MutableStateFlow("")
    private var repeatingJob: Job? = null
    private var isActive = true

    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            repeatingJob = doTemplate()
        } else {
            //return
        }
    }

    override fun onButtonClick() {
        viewModelScope.launch {
            _event.emit(ConvertEvents.Snack("app.m.appName"))
            repeatingJob?.cancel()
        }
    }

    private fun doTemplate(): Job {
        return viewModelScope.launch {
            //            while(isActive){
            convertUseCases.getExchangeRateUseCase().onEach {
                when (it) {
                    is DataState.Loading -> log("GET RATES:Loading")
                    is DataState.Failure -> log("GET RATES:Failure")
                    is DataState.Success -> {
                        log("GET RATES:Success")
                        //                        it.data.rates.forEach {
                        //                            log("GET RATES:${it.name}:${it.value}")
                        //                        }
                    }
                }
            }.launchIn(viewModelScope)
            delay(5000)
            //            }
        }
    }

    override fun onCleared() {
        repeatingJob?.cancel()
        super.onCleared()
    }
}




