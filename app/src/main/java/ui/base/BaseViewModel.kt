package ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import main.ApplicationClass

open class BaseViewModel<E : BaseEvent, A : BaseAction, S : BaseState>(
    val app: ApplicationClass,
) : ViewModel(), BaseAction {
    val action: A = this as A
    var _event = MutableSharedFlow<E>()
    val event = _event.asSharedFlow()
    var _state = MutableStateFlow(BaseState.Idle as S)
    val state = _state.asStateFlow()
    var isFirstStart = true
}

open class BaseState {
    object Idle : BaseState()
}

open class BaseEvent
interface BaseAction


