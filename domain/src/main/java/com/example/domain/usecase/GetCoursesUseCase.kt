package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return courseRepository.getCourses().onSuccess { courses ->
            courseRepository.getFavoritesCourses().onSuccess { favoritesFlow ->
                val existingIds = favoritesFlow.first().map { it.id }.toSet()
                courses
                    .filter { it.hasLike && it.id !in existingIds }
                    .forEach { courseRepository.toggleCourseFavoriteStatus(it) }
            }
        }
    }
}