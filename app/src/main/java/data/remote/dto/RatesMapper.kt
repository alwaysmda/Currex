package data.remote.dto

import com.google.gson.JsonObject
import domain.model.Rate
import util.EntityMapper
import javax.inject.Inject

class RatesMapper @Inject constructor() : EntityMapper<JsonObject, ArrayList<Rate>> {
    override fun toDomainModel(dto: JsonObject): ArrayList<Rate> {
        val rateList = arrayListOf<Rate>()
        dto.keySet().forEach {
            rateList.add(Rate(it, dto[it].toString().toDouble()))
        }
        return rateList
    }

    override fun toEntity(model: ArrayList<Rate>): JsonObject {
        val result = JsonObject()
        model.forEach {
            result.addProperty(it.name, it.value)
        }
        return result
    }

}