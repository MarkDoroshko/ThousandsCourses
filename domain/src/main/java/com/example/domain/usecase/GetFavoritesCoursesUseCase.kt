package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(): Result<Flow<List<Course>>> {
        return courseRepository.getFavoritesCourses()
    }
}