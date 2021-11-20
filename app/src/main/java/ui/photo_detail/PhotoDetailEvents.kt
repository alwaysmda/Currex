package ui.photo_detail

import main.ApplicationClass

sealed class PhotoDetailEvents {
    class Rebind(val app: ApplicationClass) : PhotoDetailEvents()
    object NavBack : PhotoDetailEvents()
    class Snack(val message: String) : PhotoDetailEvents()
    class Browse(val url: String) : PhotoDetailEvents()
}