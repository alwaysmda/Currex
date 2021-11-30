package domain.repository

import data.remote.DataState
import data.remote.DownloadDataState
import domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun download(url: String, path: String, name: String, data: String? = null): Flow<DownloadDataState>
    suspend fun getPhotoList(page: Int): DataState<List<Photo>>
    suspend fun getPhoto(id: Int): DataState<Photo>
}