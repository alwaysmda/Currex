package data.repository

import data.remote.ApiResponseHandler
import data.remote.DataState
import data.remote.PhotoApi
import domain.model.Photo
import domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import main.ApplicationClass
import util.PhotoMapper
import javax.inject.Inject


class PhotoRepositoryImpl @Inject constructor(
    private val app: ApplicationClass,
    private val api: PhotoApi,
    private val networkMapper: PhotoMapper
) : ApiResponseHandler(app), PhotoRepository {

    override suspend fun getPhotoList(page: Int): Flow<DataState<List<Photo>>> = flow {
        call(api.getPhotoList(page)).collect {
            when (it) {
                is DataState.Loading -> emit(it)
                is DataState.Failure -> emit(it)
                is DataState.Success -> {
                    val data = networkMapper.mapFromEntityList(it.data)
                    emit(DataState.Success(data))
                }
            }
        }
    }


    override suspend fun getPhoto(id: Int): Flow<DataState<Photo>> = flow {
        call(api.getPhoto(id)).collect {
            when (it) {
                is DataState.Loading -> emit(it)
                is DataState.Failure -> emit(it)
                is DataState.Success -> {
                    val data = networkMapper.toDomainModel(it.data)
                    emit(DataState.Success(data))
                }
            }
        }
    }
}