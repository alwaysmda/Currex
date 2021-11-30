package ui.photo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import data.remote.DownloadDataState
import domain.model.Photo
import domain.usecase.photo.PhotoUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import main.ApplicationClass
import util.Constant
import util.extension.getInternalDirectory
import util.extension.log
import util.extension.scanMedia
import javax.inject.Inject

@ExperimentalCoroutinesApi
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
                    is DataState.Failure -> log("VM STATE FAIL ${it.error?.code ?: 0}")
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

    override fun onDownloadClick() {
        viewModelScope.launch {
            _event.emit(PhotoDetailEvents.RequestPermission)
        }
    }

    override fun onBackClick() {
        viewModelScope.launch {
            _event.emit(PhotoDetailEvents.NavBack)
        }
    }

    override fun onPermission(granted: Boolean) {
        if (granted) {
            photoUseCases.downloadUseCase(photo.url.original, "${getInternalDirectory().path}/temp", "${photo.color.uppercase()}.png").onEach {
                when (it) {
                    is DownloadDataState.Loading     -> {
                        _state.emit(PhotoDetailState.UpdateDownloadText("Loading"))
                    }
                    is DownloadDataState.Downloading -> {
                        _state.emit(PhotoDetailState.UpdateDownloadText("Downloading ${it.percent}%"))
                    }
                    is DownloadDataState.Success     -> {
                        _state.emit(PhotoDetailState.UpdateDownloadText("Download Complete"))
                        scanMedia("${it.path}/${it.name}")
                    }
                    is DownloadDataState.Failure     -> {
                        _state.emit(PhotoDetailState.UpdateDownloadText("Download Failed"))
                    }
                }
            }.launchIn(viewModelScope)
        } else {
            viewModelScope.launch {
                _event.emit(PhotoDetailEvents.Snack("Permission Denied"))
            }
        }
    }
}




