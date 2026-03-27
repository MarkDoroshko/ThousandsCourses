package com.example.data.di

import com.example.data.remote.service.CourseApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
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
    fun provideRetrofitBuilder(
        converterFactory: Converter.Factory
    ): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(converterFactory)

    @Provides
    @Singleton
    fun provideCourseApiService(
        retrofitBuilder: Retrofit.Builder
    ): CourseApiService = retrofitBuilder
        .build()
        .create()
}