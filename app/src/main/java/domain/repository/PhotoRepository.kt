package domain.repository

import data.remote.DataState
import domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotoList(page: Int): DataState<List<Photo>>
    suspend fun getPhoto(id: Int): DataState<Photo>
}