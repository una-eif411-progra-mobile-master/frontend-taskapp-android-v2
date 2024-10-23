package edu.mike.frontend.taskapp.data.model

/**
 * Data class representing the login response returned from the API.
 *
 * @property username The username of the authenticated user.
 * @property token The JWT token returned after successful authentication.
 * @property authorities The list of authorities/privileges associated with the user.
 * @property accountNonExpired Indicates if the account is non-expired.
 * @property accountNonLocked Indicates if the account is non-locked.
 * @property credentialsNonExpired Indicates if the credentials are non-expired.
 * @property enabled Indicates if the account is enabled.
 */
data class LoginResponse(
    val username: String,
    val token: String,  // Add the token field to capture the JWT token
    val authorities: List<Authority>,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean
)

/**
 * Data class representing a user's authority or privilege.
 *
 * @property authority The name of the authority or privilege.
 */
data class Authority(
    val authority: String
)