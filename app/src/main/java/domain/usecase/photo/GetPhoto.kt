package domain.usecase.photo

import data.remote.DataState
import domain.model.Photo
import domain.repository.PhotoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPhoto @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(id: Int): Flow<DataState<Photo>> = flow {
        emit(DataState.Loading)
        delay(1000)
        val result = repository.getPhoto(id)
        emit(result)
    }
}