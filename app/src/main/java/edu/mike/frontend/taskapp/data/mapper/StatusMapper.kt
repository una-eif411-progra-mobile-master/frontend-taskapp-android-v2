package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.remote.dto.StatusDto
import edu.mike.frontend.taskapp.domain.model.Status
import javax.inject.Inject

/**
 * Mapper class for converting between Status domain entities and StatusDto data objects.
 * Provides bi-directional mapping between data layer and domain layer objects.
 */
class StatusMapper @Inject constructor() {
    /**
     * Maps a StatusDto to a domain Status entity
     *
     * @param dto The data layer status object to convert
     * @return Domain Status object
     */
    fun mapToDomain(dto: StatusDto): Status = Status(
        id = dto.id, label = dto.label
    )

    /**
     * Maps a domain Status to a StatusDto
     *
     * @param domain The domain layer status object to convert
     * @return StatusDto object for data layer
     */
    fun mapToDto(domain: Status): StatusDto = StatusDto(
        id = domain.id, label = domain.label
    )
}