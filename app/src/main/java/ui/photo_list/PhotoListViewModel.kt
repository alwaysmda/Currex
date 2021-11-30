package ui.photo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.remote.DataState
import domain.model.Photo
import domain.model.Photo.Companion.cloned
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
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class PhotoListViewModel @Inject constructor(
    val app: ApplicationClass,
    private val photoUseCases: PhotoUseCases,
) : ViewModel(), PhotoListAction {

    val action: PhotoListAction = this

    private var _state = MutableStateFlow<PhotoListState>(PhotoListState.Idle)
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<PhotoListEvents>()
    val event = _event.asSharedFlow()

    private val list = arrayListOf<Photo>()

    private var isFirstStart = true
    private var page = 1
    private var isPaginating = false
    private var isLastPage = false

    private fun getTemplateList() {
        isPaginating = true
        photoUseCases.getPhotoList(page).onEach {
            when (it) {
                is DataState.Loading -> {
                    if (list.isEmpty()) {
                        _state.emit(PhotoListState.Loading)
                    } else {
                        _state.emit(PhotoListState.UpdatePhotoList(list.cloned(), loading = true, retry = false))
                    }
                }
                is DataState.Success -> {
                    isLastPage = page == 3 //it.data.isEmpty()
                    isPaginating = false
                    list.addAll(it.data)
                    _state.emit(PhotoListState.UpdatePhotoList(list.cloned(), isLastPage.not(), false))
                }
                is DataState.Failure -> {
                    _event.emit(PhotoListEvents.Snack(it.error?.message ?: "something went wrong"))
                    if (list.isEmpty()) {
                        _state.emit(PhotoListState.EmptyView)
                    } else {
                        _state.emit(PhotoListState.UpdatePhotoList(list.cloned(), loading = false, retry = true))
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onStart() {
        if (isFirstStart) {
            isFirstStart = false
            getTemplateList()
        } else {
            //return
        }
    }

    override fun onPaginate() {
        if (isPaginating.not() && isLastPage.not()) {
            isPaginating = true
            page++
            getTemplateList()
        }
    }

    override fun onRetry() {
        getTemplateList()
    }

    override fun onTitleClick() {
        viewModelScope.launch {
            if (app.languageManager.currentLanguage == Constant.Languages.EN) {
                app.languageManager.changeLang(Constant.Languages.FA)
            } else {
                app.languageManager.changeLang(Constant.Languages.EN)
            }
            _event.emit(PhotoListEvents.Rebind(app))
            _event.emit(PhotoListEvents.Snack(app.m.languageUpdated))
        }
    }

    override fun onPhotoClick(position: Int, data: Photo) {
        viewModelScope.launch {
            _event.emit(PhotoListEvents.NavPhoto(data, position))
        }
    }
}




