package edu.mike.frontend.taskapp.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.data.di.NetworkModule.BASE_URL
import edu.mike.frontend.taskapp.data.local.AuthPreferences
import edu.mike.frontend.taskapp.data.remote.api.AuthService
import edu.mike.frontend.taskapp.data.remote.api.TaskService
import edu.mike.frontend.taskapp.data.remote.dto.AuthResponseDto
import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import edu.mike.frontend.taskapp.data.remote.interceptor.AuthInterceptor
import edu.mike.frontend.taskapp.data.remote.interceptor.ResponseInterceptor
import edu.mike.frontend.taskapp.data.remote.serializer.AuthResponseDeserializer
import edu.mike.frontend.taskapp.data.remote.serializer.TaskDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides network-related dependencies for the application.
 *
 * This module is responsible for providing singleton instances of:
 * - Gson for JSON serialization/deserialization
 * - HTTP client configuration with logging
 * - Retrofit service configuration
 * - API service interfaces
 *
 * @property BASE_URL The base URL for the API endpoints
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:8080/v1/" // TODO: Localhost for emulator
    private const val DATE_FORMAT = "yyyy-MM-dd"

    /**
     * Provides a singleton Gson instance configured with custom type adapters.
     *
     * @return Configured [Gson] instance
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(TaskDto::class.java, TaskDeserializer())
        .registerTypeAdapter(AuthResponseDto::class.java, AuthResponseDeserializer())
        .setDateFormat(DATE_FORMAT)
        .create()

    /**
     * Provides a logging interceptor for HTTP request/response logging.
     *
     * @return Configured [HttpLoggingInterceptor]
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    /**
     * Provides the auth interceptor for adding authentication headers to requests.
     *
     * @param authPreferences The preferences storing authentication data
     * @return Configured [AuthInterceptor]
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(authPreferences: AuthPreferences): AuthInterceptor =
        AuthInterceptor(authPreferences)

    /**
     * Provides a configured OkHttpClient with interceptors.
     *
     * @param loggingInterceptor For logging HTTP traffic
     * @param responseInterceptor For handling API responses
     * @param authInterceptor For adding authentication headers to requests
     * @return Configured [OkHttpClient]
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        responseInterceptor: ResponseInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(responseInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Provides a configured Retrofit instance.
     *
     * @param okHttpClient The HTTP client to use
     * @param gson The Gson instance for JSON conversion
     * @return Configured [Retrofit] instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    /**
     * Provides the TaskService implementation.
     *
     * @param retrofit The Retrofit instance
     * @return Implementation of [TaskService]
     */
    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)

    /**
     * Provides the AuthService implementation for authentication operations.
     *
     * @param retrofit The Retrofit instance
     * @return Implementation of [AuthService]
     */
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}