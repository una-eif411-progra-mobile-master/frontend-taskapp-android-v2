package edu.mike.frontend.taskapp.domain.repository

     /**
      * Repository interface for authentication-related operations.
      * Defines methods for user authentication and session management.
      */
     interface AuthRepository {
         /**
          * Authenticates a user with the provided credentials.
          *
          * @param username The user's username
          * @param password The user's password
          * @return [Result] indicating success or failure of the authentication
          */
         suspend fun login(username: String, password: String): Result<Unit>

         /**
          * Ends the user's authenticated session.
          *
          * @return [Result] indicating success or failure of the logout operation
          */
         suspend fun logout(): Result<Unit>

         /**
          * Checks if the user is currently authenticated.
          *
          * @return [Result] wrapping a boolean indicating authentication status
          */
         suspend fun isAuthenticated(): Result<Boolean>

         /**
          * Retrieves the current user's username if authenticated.
          *
          * @return [Result] wrapping the authenticated username or null
          */
         suspend fun getCurrentUser(): Result<String?>
     }