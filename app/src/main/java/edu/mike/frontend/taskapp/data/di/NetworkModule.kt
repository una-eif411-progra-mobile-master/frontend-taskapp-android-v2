package edu.mike.frontend.taskapp.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.data.remote.api.TaskService
import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import edu.mike.frontend.taskapp.data.remote.interceptor.ResponseInterceptor
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
 * - HTTP client configuration with logging and custom interceptors
 * - Retrofit service configuration
 * - API service interfaces
 *
 * @property BASE_URL The base URL for the API endpoints
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://628ae68e667aea3a3e23e474.mockapi.io/api/v1/"
    private const val DATE_FORMAT = "yyyy-MM-dd"

    /**
     * Provides a singleton [Gson] instance configured with custom type adapters and date format.
     *
     * @return Configured [Gson] instance
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(TaskDto::class.java, TaskDeserializer())
        .setDateFormat(DATE_FORMAT)
        .create()

    /**
     * Provides a singleton [HttpLoggingInterceptor] for HTTP request/response logging.
     *
     * @return Configured [HttpLoggingInterceptor]
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /**
     * Provides a singleton [OkHttpClient] configured with logging and response interceptors,
     * and reasonable timeout settings.
     *
     * @param loggingInterceptor For logging HTTP traffic
     * @param responseInterceptor For handling API responses globally
     * @return Configured [OkHttpClient]
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        responseInterceptor: ResponseInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(responseInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Provides a singleton [Retrofit] instance configured with Gson converter and OkHttp client.
     *
     * @param okHttpClient The configured HTTP client
     * @param gson The Gson instance for JSON serialization
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
     * Provides the implementation of the [TaskService] API interface.
     *
     * @param retrofit The Retrofit instance used to create the service
     * @return Implementation of [TaskService]
     */
    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService =
        retrofit.create(TaskService::class.java)
}