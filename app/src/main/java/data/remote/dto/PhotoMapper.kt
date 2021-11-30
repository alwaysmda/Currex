package data.remote.dto

import domain.model.Photo
import domain.model.Url
import util.EntityMapper
import javax.inject.Inject

class PhotoMapper @Inject constructor() : EntityMapper<PhotoDto, Photo> {
    override fun toDomainModel(dto: PhotoDto): Photo =
        Photo(
            dto.albumId,
            dto.id,
            dto.title,
            dto.url.split("/").last(),
            Url(dto.thumbnailUrl, dto.url),
        )


    override fun toEntity(model: Photo): PhotoDto =
        PhotoDto(
            model.albumId,
            model.id,
            model.title,
            model.url.original,
            model.url.thumb,
        )

    fun fromEntityList(dtoList: ArrayList<PhotoDto>): ArrayList<Photo> =
        ArrayList(dtoList.map { toDomainModel(it) })
}