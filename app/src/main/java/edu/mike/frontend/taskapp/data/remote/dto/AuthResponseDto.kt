package edu.mike.frontend.taskapp.data.remote.dto

/**
 * DTO representing authentication response
 */
data class AuthResponseDto(
    val token: String,
    val userId: String
)