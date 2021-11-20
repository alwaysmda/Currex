package ui.photo_list

import domain.model.Photo

sealed class PhotoListState {
    object Idle : PhotoListState()
    object Loading : PhotoListState()
    object EmptyView : PhotoListState()
    class UpdatePhotoList(val list: List<Photo>, val loading: Boolean, val retry: Boolean) : PhotoListState()
}

