package ui.photo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.model.Photo
import domain.usecase.PhotoUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import util.Constant
import util.extension.log
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    val app: ApplicationClass,
    private val photoUseCases: PhotoUseCases,
) : ViewModel(), PhotoDetailAction {

    val action: PhotoDetailAction = this

    private var _state = MutableStateFlow<PhotoDetailState>(PhotoDetailState.Idle)
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<PhotoDetailEvents>()
    val event = _event.asSharedFlow()


    private var isFirstStart = true
    private lateinit var photo: Photo


    override fun onStart(photo: Photo) {
        if (isFirstStart) {
            isFirstStart = false
            this.photo = photo
            viewModelScope.launch {
                _state.emit(PhotoDetailState.Bind(photo))
            }
        } else {
            //return
        }
    }

    override fun onTitleClick() {
        viewModelScope.launch {
            val theme = if (app.themeManager.currentTheme.value == Constant.Themes.LIGHT_PINK.value) Constant.Themes.DARK_BLUE else Constant.Themes.LIGHT_PINK
            _event.emit(PhotoDetailEvents.UpdateTheme(theme))
            photoUseCases.getPhoto(30000000).onEach {
                when (it) {
                    is DataState.Loading -> log("VM STATE LOADING")
                    is DataState.Failure -> log("VM STATE FAIL ${it.error.code}")
                    is DataState.Success -> log("VM STATE SUCCESS ${it.data.id}")
                }
            }.launchIn(viewModelScope)
        }
    }

    override fun onOpenUrlClick() {
        viewModelScope.launch {
            _event.emit(PhotoDetailEvents.Browse(photo.url.original))
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _event.emit(PhotoDetailEvents.NavBack)
        }
    }
}




