package ui.photo_list

import domain.model.Photo

interface PhotoListAction {
    fun onStart()
    fun onRetry()
    fun onPaginate()
    fun onTitleClick()
    fun onPhotoClick(position: Int, data: Photo)
}