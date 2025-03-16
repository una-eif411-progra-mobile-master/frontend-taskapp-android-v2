package edu.mike.frontend.taskapp.domain.error

/**
 * Domain error class for handling errors in the domain layer
 */
sealed class DomainError : Exception() {
    data class TaskError(override val message: String) : DomainError()
    data class NetworkError(override val message: String) : DomainError()
    data class MappingError(override val message: String) : DomainError()
    data object UnknownError : DomainError() {
        override val message: String = "An unknown error occurred"
    }
}