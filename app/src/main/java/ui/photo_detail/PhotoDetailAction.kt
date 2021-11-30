package ui.photo_detail

import domain.model.Photo

interface PhotoDetailAction {
    fun onStart(photo: Photo)
    fun onBackClick()
    fun onTitleClick()
    fun onOpenUrlClick()
    fun onDownloadClick()
    fun onPermission(granted: Boolean)
}