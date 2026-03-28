package com.example.domain.repository

import com.example.domain.entity.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>

    suspend fun getFavoritesCourses(): Result<Flow<List<Course>>>

    suspend fun toggleCourseFavoriteStatus(course: Course): Result<Unit>

    suspend fun getCourseFromFavorites(courseId: Int): Result<Course?>
}