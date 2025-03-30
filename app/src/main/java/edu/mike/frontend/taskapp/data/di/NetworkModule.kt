package edu.mike.frontend.taskapp.data.di

     import com.google.gson.Gson
     import com.google.gson.GsonBuilder
     import dagger.Module
     import dagger.Provides
     import dagger.hilt.InstallIn
     import dagger.hilt.components.SingletonComponent
     import edu.mike.frontend.taskapp.data.model.TaskDto
     import okhttp3.OkHttpClient
     import okhttp3.logging.HttpLoggingInterceptor
     import retrofit2.Retrofit
     import retrofit2.converter.gson.GsonConverterFactory
     import javax.inject.Singleton

     @Module
     @InstallIn(SingletonComponent::class)
     object NetworkModule {

         @Provides
         @Singleton
         fun provideGson(): Gson {
             return GsonBuilder()
                 .registerTypeAdapter(
                     TaskDto::class.java,
                     TaskDeserializer()
                 )
                 .setDateFormat("yyyy-MM-dd")
                 .create()
         }

         @Provides
         @Singleton
         fun provideLoggingInterceptor(): HttpLoggingInterceptor {
             return HttpLoggingInterceptor().apply {
                 level = HttpLoggingInterceptor.Level.BODY
             }
         }

         @Provides
         @Singleton
         fun provideOkHttpClient(
             loggingInterceptor: HttpLoggingInterceptor,
             responseInterceptor: ResponseInterceptor
         ): OkHttpClient {
             return OkHttpClient.Builder()
                 .addInterceptor(loggingInterceptor)
                 .addInterceptor(responseInterceptor)
                 .build()
         }

         @Provides
         @Singleton
         fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
             return Retrofit.Builder()
                 .baseUrl("https://628ae68e667aea3a3e23e474.mockapi.io/api/v1/")
                 .addConverterFactory(GsonConverterFactory.create(gson))
                 .client(okHttpClient)
                 .build()
         }

         @Provides
         @Singleton
         fun provideTaskService(retrofit: Retrofit): TaskService {
             return retrofit.create(TaskService::class.java)
         }
     }