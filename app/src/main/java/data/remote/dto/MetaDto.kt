package data.remote.dto

import com.google.gson.annotations.SerializedName

data class MetaDto(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)