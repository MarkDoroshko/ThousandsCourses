package com.example.data.di

import com.example.data.remote.service.CourseApiService
import com.example.data.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideConverterFactory(
        json: Json
    ): Converter.Factory = json.asConverterFactory(
        "application/json".toMediaType()
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            response.newBuilder()
                .header("Content-Type", "application/json")
                .build()
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)

    @Provides
    @Singleton
    fun provideCourseApiService(
        retrofitBuilder: Retrofit.Builder
    ): CourseApiService = retrofitBuilder
        .build()
        .create()
}