package domain.usecase

import data.remote.DataState
import domain.model.Photo
import domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoList @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(page: Int): Flow<DataState<List<Photo>>> {
        return repository.getPhotoList(page)
    }
}