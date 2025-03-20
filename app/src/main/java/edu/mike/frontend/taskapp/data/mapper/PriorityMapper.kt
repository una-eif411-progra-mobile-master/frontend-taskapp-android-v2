package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.datasource.model.PriorityDto
import edu.mike.frontend.taskapp.domain.model.Priority

/**
 * Mapper class for converting between Priority domain entities and PriorityDto data objects
 */
class PriorityMapper {
    /**
     * Maps a PriorityDto to a domain Priority entity
     * @param dto The data layer priority object to convert
     * @return Domain Priority object
     */
    fun mapToDomain(dto: PriorityDto): Priority = Priority(
        id = dto.id,
        label = dto.label
    )

    /**
     * Maps a domain Priority to a PriorityDto
     * @param domain The domain layer priority object to convert
     * @return PriorityDto object for data layer
     */
    fun mapToDto(domain: Priority): PriorityDto = PriorityDto(
        id = domain.id,
        label = domain.label
    )
}