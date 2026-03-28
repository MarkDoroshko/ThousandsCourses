package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return courseRepository.getCourses().onSuccess { courses ->
            courses.forEach { course ->
                if (course.hasLike) {
                    courseRepository.getCourseFromFavorites(course.id).onSuccess { courseFromFavorites ->
                        if (courseFromFavorites == null) {
                            courseRepository.toggleCourseFavoriteStatus(course)
                        }
                    }
                }
            }
        }
    }
}