package com.example.data.di

import com.example.data.repository.CourseRepositoryImpl
import com.example.domain.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository
}