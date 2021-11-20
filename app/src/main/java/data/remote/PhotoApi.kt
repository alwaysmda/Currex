package data.remote

import data.remote.dto.PhotoDto
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PhotoApi {
    @POST("post")
    suspend fun post(@FieldMap body: Map<String, String>): Response<String>

    @GET("albums/{page}/photos")
    suspend fun getPhotoList(@Path(value = "page", encoded = true) page: Int): Response<List<PhotoDto>>

    @GET("photosx/{photo_id}")
    suspend fun getPhoto(@Path(value = "photo_id", encoded = true) photoId: Int): Response<PhotoDto>

}