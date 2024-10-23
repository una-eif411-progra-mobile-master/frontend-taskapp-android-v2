package edu.mike.frontend.taskapp.data.model

/**
 * This class represents the response of a login request
 */
data class LoginResponse(val token: String, val userId: Long)