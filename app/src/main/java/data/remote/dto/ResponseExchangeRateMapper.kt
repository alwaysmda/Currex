package data.remote.dto

import com.google.gson.JsonObject
import domain.model.Rate
import domain.model.ResponseExchangeRate
import util.EntityMapper
import javax.inject.Inject

class ResponseExchangeRateMapper @Inject constructor() : EntityMapper<ResponseExchangeRateDto, ResponseExchangeRate> {
    override fun toDomainModel(dto: ResponseExchangeRateDto): ResponseExchangeRate {
        val rateList = arrayListOf<Rate>()
        dto.rates.keySet().forEach {
            rateList.add(Rate(it, dto.rates[it].toString().toDouble()))
        }
        return ResponseExchangeRate(
            dto.success,
            dto.timestamp,
            dto.base,
            dto.date,
            rateList,
        )
    }

    override fun toEntity(model: ResponseExchangeRate): ResponseExchangeRateDto =
        ResponseExchangeRateDto(
            model.success,
            model.timestamp,
            model.base,
            model.date,
            JsonObject(),
        )

    fun fromEntityList(dtoList: ArrayList<ResponseExchangeRateDto>): ArrayList<ResponseExchangeRate> =
        ArrayList(dtoList.map { toDomainModel(it) })
}