package ui.photo_list

import domain.model.Photo
import main.ApplicationClass

sealed class PhotoListEvents {
    class Rebind(val app: ApplicationClass) : PhotoListEvents()
    class NavPhoto(val photo: Photo, val position: Int) : PhotoListEvents()
    class Snack(val message: String) : PhotoListEvents()
}