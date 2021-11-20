package domain.repository

import data.remote.DataState
import domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getPhotoList(page: Int): Flow<DataState<List<Photo>>>
    suspend fun getPhoto(id: Int): Flow<DataState<Photo>>
}