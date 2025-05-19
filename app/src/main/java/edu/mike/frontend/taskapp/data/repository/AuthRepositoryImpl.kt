package edu.mike.frontend.taskapp.data.repository

            import edu.mike.frontend.taskapp.data.local.AuthPreferences
            import edu.mike.frontend.taskapp.data.remote.AuthRemoteDataSource
            import edu.mike.frontend.taskapp.domain.model.Credentials
            import edu.mike.frontend.taskapp.domain.repository.AuthRepository
            import javax.inject.Inject
            import javax.inject.Singleton

            /**
             * Implementation of [AuthRepository] that manages authentication using
             * a remote service and local session storage.
             */
            @Singleton
            class AuthRepositoryImpl @Inject constructor(
                private val authRemoteDataSource: AuthRemoteDataSource,
                private val authPreferences: AuthPreferences
            ) : AuthRepository {

                override suspend fun login(username: String, password: String): Result<Unit> {
                    return try {
                        val credentials = Credentials(username, password)
                        authRemoteDataSource.login(credentials)
                            .onSuccess { authResult ->
                                // Save auth state to preferences
                                authPreferences.saveAuthToken(authResult.token)
                                authPreferences.saveUsername(username)
                            }
                            .map { Unit }
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }

                override suspend fun logout(): Result<Unit> {
                    return try {
                        // Call remote logout if needed
                        authRemoteDataSource.logout()
                        // Clear local auth state
                        authPreferences.clearAuthData()
                        Result.success(Unit)
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }

                override suspend fun isAuthenticated(): Result<Boolean> {
                    return try {
                        val token = authPreferences.getAuthToken()
                        Result.success(token != null && token.isNotEmpty())
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }

                override suspend fun getCurrentUser(): Result<String?> {
                    return try {
                        val username = authPreferences.getUsername()
                        Result.success(username)
                    } catch (e: Exception) {
                        Result.failure(e)
                    }
                }
            }