package com.example.domain.repository

import com.example.domain.entity.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>

    suspend fun getFavoritesCourses(): Result<Flow<List<Course>>>

    suspend fun toggleCourseFavoriteStatus(id: Int): Result<Unit>
}