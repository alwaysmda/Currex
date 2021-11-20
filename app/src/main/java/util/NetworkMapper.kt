package util

import data.remote.dto.PhotoDto
import domain.model.Photo
import domain.model.Url
import javax.inject.Inject

class PhotoMapper @Inject constructor() : EntityMapper<PhotoDto, Photo> {
    override fun toDomainModel(dto: PhotoDto): Photo =
        Photo(
            dto.albumId,
            dto.id,
            dto.title,
            Url(dto.thumbnailUrl, dto.url)
        )


    override fun toEntity(model: Photo): PhotoDto =
        PhotoDto(
            model.albumId,
            model.id,
            model.title,
            model.url.original,
            model.url.thumb,
        )

    fun mapFromEntityList(photoDtoList: List<PhotoDto>): List<Photo> =
        photoDtoList.map { toDomainModel(it) }
}
