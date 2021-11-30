package data.remote

import data.remote.dto.PhotoDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url

interface PhotoApi {
    @Streaming
    @GET
    suspend fun download(@Url url: String): Response<ResponseBody>

    @POST("post")
    suspend fun post(@FieldMap body: Map<String, String>): Response<String>

    @GET("albums/{page}/photos")
    suspend fun getPhotoList(@Path(value = "page", encoded = true) page: Int): Response<ArrayList<PhotoDto>>

    @GET("photosx/{photo_id}")
    suspend fun getPhoto(@Path(value = "photo_id", encoded = true) photoId: Int): Response<PhotoDto>

}