package edu.mike.frontend.taskapp.data.network

import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit service interface for Login-related API endpoints.
 */
interface LoginService {
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}