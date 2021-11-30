package data.remote.dto

import domain.model.NetworkError
import util.EntityMapper
import javax.inject.Inject

class NetworkErrorMapper @Inject constructor() : EntityMapper<NetworkErrorDto, NetworkError> {
    override fun toDomainModel(dto: NetworkErrorDto): NetworkError =
        NetworkError(
            dto.meta.code,
            dto.meta.message,
        )


    override fun toEntity(model: NetworkError): NetworkErrorDto =
        NetworkErrorDto(
            "fail",
            MetaDto(model.code, model.message),
            null,
        )

    fun fromEntityList(dtoList: ArrayList<NetworkErrorDto>): ArrayList<NetworkError> =
        ArrayList(dtoList.map { toDomainModel(it) })
}