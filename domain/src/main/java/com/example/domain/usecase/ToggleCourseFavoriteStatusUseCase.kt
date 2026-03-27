package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import javax.inject.Inject

class ToggleCourseFavoriteStatusUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(course: Course): Result<Unit> {
        return courseRepository.toggleCourseFavoriteStatus(course)
    }
}