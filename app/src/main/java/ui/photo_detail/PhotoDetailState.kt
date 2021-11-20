package ui.photo_detail

import domain.model.Photo

sealed class PhotoDetailState {
    object Idle : PhotoDetailState()
    class Bind(val photo: Photo) : PhotoDetailState()
}

