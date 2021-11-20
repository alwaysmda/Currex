package data.remote

import domain.model.NetworkError

sealed class DataState<out T> {
    class Success<out D>(val data: D) : DataState<D>()
    class Failure(val error: NetworkError) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}