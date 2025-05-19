package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.remote.dto.AuthRequestDto
import edu.mike.frontend.taskapp.data.remote.dto.AuthResponseDto
import edu.mike.frontend.taskapp.domain.model.AuthResult
import edu.mike.frontend.taskapp.domain.model.Credentials

/**
 * Mapper for converting between Auth DTOs and domain models
 */
object AuthMapper {
    /**
     * Converts credentials to DTO
     */
    fun credentialsToDto(credentials: Credentials): AuthRequestDto {
        return AuthRequestDto(
            username = credentials.username,
            password = credentials.password
        )
    }

    /**
     * Converts authentication response DTO to domain model
     */
    fun dtoToAuthResult(dto: AuthResponseDto): AuthResult {
        return AuthResult(
            token = dto.token,
            userId = dto.userId
        )
    }
}