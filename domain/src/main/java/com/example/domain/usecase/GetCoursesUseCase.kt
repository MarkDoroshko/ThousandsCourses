package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return courseRepository.getCourses()
    }
}