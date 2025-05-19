package edu.mike.frontend.taskapp.domain.model
/**
 * Domain model representing authentication result
 */
data class AuthResult(
    val token: String,
    val userId: String
)