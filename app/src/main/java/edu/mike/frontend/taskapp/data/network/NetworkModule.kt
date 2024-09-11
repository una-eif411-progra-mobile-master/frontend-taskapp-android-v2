package edu.mike.frontend.taskapp.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.data.model.Task
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Dagger module to provide network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of Gson configured with a custom deserializer for Task.
     *
     * @return A configured Gson instance.
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                Task::class.java,
                TaskDeserializer()
            )
            .setDateFormat("yyyy-MM-dd")
            .create()
    }

    /**
     * Provides a singleton instance of HttpLoggingInterceptor for logging HTTP requests and responses.
     *
     * @return A configured HttpLoggingInterceptor instance.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Provides a singleton instance of OkHttpClient configured with the logging interceptor.
     *
     * @param loggingInterceptor The HttpLoggingInterceptor instance.
     * @return A configured OkHttpClient instance.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides a singleton instance of Retrofit configured with the base URL, Gson converter, and OkHttpClient.
     *
     * @param okHttpClient The OkHttpClient instance.
     * @param gson The Gson instance.
     * @return A configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://628ae68e667aea3a3e23e474.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    /**
     * Provides a singleton instance of TaskService created by Retrofit.
     *
     * @param retrofit The Retrofit instance.
     * @return A TaskService instance.
     */
    @Provides
    @Singleton
    fun provideTaskService(retrofit: Retrofit): TaskService {
        return retrofit.create(TaskService::class.java)
    }
}