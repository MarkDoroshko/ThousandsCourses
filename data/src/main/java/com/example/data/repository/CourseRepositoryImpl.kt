package com.example.data.repository

import com.example.data.local.dao.CoursesDao
import com.example.data.local.mapper.toDbModel
import com.example.data.local.mapper.toEntities
import com.example.data.remote.mapper.toListCourse
import com.example.data.remote.service.CourseApiService
import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val courseApiService: CourseApiService,
    private val coursesDao: CoursesDao
) : CourseRepository {
    override suspend fun getCourses(): Result<List<Course>> {
        return runCatching { courseApiService.getCourses() }
            .mapCatching { it.toListCourse() }
    }

    override suspend fun getFavoritesCourses(): Result<Flow<List<Course>>> {
        return runCatching { coursesDao.getCourses() }
            .mapCatching { it.map { coursesDbModel -> coursesDbModel.toEntities() } }
    }

    override suspend fun toggleCourseFavoriteStatus(course: Course): Result<Unit> {
        return runCatching {
            if (coursesDao.getCourse(course.id) != null) {
                coursesDao.deleteCourse(course.id)
            } else {
                coursesDao.addCourse(course.toDbModel())
            }
        }
    }
}