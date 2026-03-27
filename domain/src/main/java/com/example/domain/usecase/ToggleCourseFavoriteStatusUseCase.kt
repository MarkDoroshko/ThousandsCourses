package com.example.domain.usecase

import com.example.domain.repository.CourseRepository
import javax.inject.Inject

class ToggleCourseFavoriteStatusUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return courseRepository.toggleCourseFavoriteStatus(id)
    }
}