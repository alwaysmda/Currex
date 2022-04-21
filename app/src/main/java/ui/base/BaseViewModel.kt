package ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import main.ApplicationClass

open class BaseViewModel<E : BaseEvent, A : BaseAction>(
    val app: ApplicationClass,
) : ViewModel(), BaseAction {
    val action: A = this as A
    var _event = MutableSharedFlow<E>()
    val event = _event.asSharedFlow()
    var isFirstStart = true
}

open class BaseEvent
interface BaseAction


