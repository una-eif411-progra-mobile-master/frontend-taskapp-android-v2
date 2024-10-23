package edu.mike.frontend.taskapp.data.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.BuildConfig
import edu.mike.frontend.taskapp.data.model.Task
import edu.mike.frontend.taskapp.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module to provide network-related dependencies for the application.
 * This module includes configuration for Retrofit, OkHttp, Gson, and a logging interceptor.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of TokenManager.
     *
     * @param context The application context.
     * @return A TokenManager instance.
     */
    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    /**
     * Provides a singleton instance of Gson configured with a custom deserializer for Task.
     *
     * @return A configured Gson instance with a date format and custom deserializer.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                Task::class.java,
                TaskDeserializer() // Register custom deserializer for Task class
            )
            .setDateFormat("yyyy-MM-dd") // Set date format for Gson
            .create()
    }

    /**
     * Provides a singleton instance of HttpLoggingInterceptor for logging HTTP requests and responses.
     * This interceptor is useful for debugging network calls and checking the API responses.
     *
     * @return A configured HttpLoggingInterceptor instance that logs the body of requests and responses.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Log request and response body
        }
    }

    /**
     * Provides an interceptor to add the JWT token to the Authorization header for every request.
     *
     * @param tokenManager The TokenManager instance to get the stored token.
     * @return A configured Interceptor that adds the token to each request.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            tokenManager.getToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    /**
     * Provides a singleton instance of OkHttpClient configured with the logging and authentication interceptors.
     * OkHttpClient handles the network layer and intercepts the HTTP requests.
     *
     * @param loggingInterceptor The HttpLoggingInterceptor instance to log the network traffic.
     * @param authInterceptor The Interceptor to add the JWT token to each request.
     * @return A configured OkHttpClient instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging interceptor to OkHttpClient
            .addInterceptor(authInterceptor) // Add token interceptor
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit configured with the base URL, Gson converter, and OkHttpClient.
     * Retrofit handles making HTTP requests to the server and converting the responses.
     *
     * @param okHttpClient The OkHttpClient instance that manages HTTP requests.
     * @param gson The Gson instance for JSON conversion.
     * @return A configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Use the base URL from the configuration object
            .addConverterFactory(GsonConverterFactory.create(gson)) // Add Gson converter for JSON parsing
            .client(okHttpClient) // Attach OkHttpClient for making network requests
            .build()
    }

    /**
     * Provides a singleton instance of TaskService created by Retrofit.
     * This service is used to define and make API calls related to tasks.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return A TaskService instance to handle API requests.
     */
    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService {
        return retrofit.create(TaskService::class.java)
    }

    /**
     * Provides a singleton instance of LoginService created by Retrofit.
     * This service is used to define and make API calls related to user login.
     *
     * @param retrofit The Retrofit instance used to create the service.
     * @return A LoginService instance to handle API requests.
     */
    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }
}