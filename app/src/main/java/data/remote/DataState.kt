package data.remote

import util.StringResource

sealed class DataState<out T> {
    class Success<out D>(val data: D) : DataState<D>()
    class Failure(val code: Int, val message: StringResource.Translatable) : DataState<Nothing>() {
        companion object {
            const val CODE_INVALID = -1001
            const val CODE_NOT_FOUND = -1002
            const val CODE_NETWORK_FAILURE = -1003
            const val CODE_DUPLICATE = -1004
            const val CODE_NO_INTERNET = -1005
            const val CODE_PARSE_EXCEPTION = -1006
        }
    }

    object Loading : DataState<Nothing>()
}