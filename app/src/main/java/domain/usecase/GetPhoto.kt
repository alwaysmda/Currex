package domain.usecase

import data.remote.DataState
import domain.model.Photo
import domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhoto @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(id: Int): Flow<DataState<Photo>> {
        return repository.getPhoto(id)
    }
}