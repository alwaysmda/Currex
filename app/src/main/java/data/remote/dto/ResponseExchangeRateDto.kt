package data.remote.dto

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class ResponseExchangeRateDto(
    @SerializedName("success") var success: Boolean,
    @SerializedName("timestamp") var timestamp: Long,
    @SerializedName("base") var base: String,
    @SerializedName("date") var date: String,
    @SerializedName("rates") var rates: JsonObject,
)