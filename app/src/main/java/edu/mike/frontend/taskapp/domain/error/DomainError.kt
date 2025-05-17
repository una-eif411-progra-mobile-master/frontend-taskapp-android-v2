package edu.mike.frontend.taskapp.domain.error

/**
 * Domain error class for handling errors in the domain layer
 */
sealed class DomainError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    class TaskError(
        message: String,
        cause: Throwable? = null
    ) : DomainError(message, cause)

    class NetworkError(
        message: String,
        cause: Throwable? = null
    ) : DomainError(message, cause)

    class MappingError(
        message: String,
        cause: Throwable? = null
    ) : DomainError(message, cause)

    class UnknownError(
        message: String = "An unknown error occurred",
        cause: Throwable? = null
    ) : DomainError(message, cause)
}