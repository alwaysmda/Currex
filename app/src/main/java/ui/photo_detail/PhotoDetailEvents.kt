package ui.photo_detail

import main.ApplicationClass
import util.Constant

sealed class PhotoDetailEvents {
    class Rebind(val app: ApplicationClass) : PhotoDetailEvents()
    object NavBack : PhotoDetailEvents()
    class Snack(val message: String) : PhotoDetailEvents()
    class Browse(val url: String) : PhotoDetailEvents()
    class UpdateTheme(val theme: Constant.Themes) : PhotoDetailEvents()
}