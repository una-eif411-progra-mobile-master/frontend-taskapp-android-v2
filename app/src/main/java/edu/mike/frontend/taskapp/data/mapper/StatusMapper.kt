package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.datasource.model.StatusDto
import edu.mike.frontend.taskapp.domain.model.Status

/**
 * Mapper class for converting between Status domain entities and StatusDto data objects
 */
class StatusMapper {
    /**
     * Maps a StatusDto to a domain Status entity
     * @param dto The data layer status object to convert
     * @return Domain Status object
     */
    fun mapToDomain(dto: StatusDto): Status = Status(
        id = dto.id, label = dto.label
    )

    /**
     * Maps a domain Status to a StatusDto
     * @param domain The domain layer status object to convert
     * @return StatusDto object for data layer
     */
    fun mapToDto(domain: Status): StatusDto = StatusDto(
        id = domain.id, label = domain.label
    )
}