package data.repository

import data.remote.ApiResponseHandler
import data.remote.DataState
import data.remote.PhotoApi
import domain.model.Photo
import domain.repository.PhotoRepository
import main.ApplicationClass
import util.PhotoMapper
import javax.inject.Inject


class PhotoRepositoryImpl @Inject constructor(
    private val app: ApplicationClass,
    private val api: PhotoApi,
    private val networkMapper: PhotoMapper
) : ApiResponseHandler(app), PhotoRepository {

    override suspend fun getPhotoList(page: Int): DataState<List<Photo>> {
        return when (val response = call(api.getPhotoList(page))) {
            is DataState.Loading -> response
            is DataState.Failure -> response
            is DataState.Success -> {
                val data = networkMapper.mapFromEntityList(response.data)
                DataState.Success(data)
            }
        }
    }


    override suspend fun getPhoto(id: Int): DataState<Photo> {
        return when (val response = call(api.getPhoto(id))) {
            is DataState.Loading -> response
            is DataState.Failure -> response
            is DataState.Success -> {
                val data = networkMapper.toDomainModel(response.data)
                DataState.Success(data)
            }
        }
    }
}