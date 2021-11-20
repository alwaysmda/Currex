package util

interface EntityMapper<Entity, DomainModel> {
    fun toDomainModel(dto: Entity): DomainModel
    fun toEntity(model: DomainModel): Entity
}