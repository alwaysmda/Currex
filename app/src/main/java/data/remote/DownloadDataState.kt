package data.remote

import domain.model.NetworkError

sealed class DownloadDataState {
    class Success(val path: String, val name: String, val data: String?) : DownloadDataState()
    class Failure(val code: Int, val message: String, val error: NetworkError? = null) : DownloadDataState() {
        companion object {
            const val CODE_INVALID = -1001
            const val CODE_NOT_FOUND = -1002
            const val CODE_NETWORK_FAILURE = -1003
            const val CODE_DUPLICATE = -1004
            const val CODE_NO_INTERNET = -1005
            const val CODE_PARSE_EXCEPTION = -1006
        }
    }

    class Downloading(val percent: Int) : DownloadDataState()
    object Loading : DownloadDataState()
}
