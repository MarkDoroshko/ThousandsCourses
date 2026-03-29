package com.example.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.local.database.CoursesDatabase
import com.example.data.remote.dto.CoursesResponse
import com.example.data.remote.service.CourseApiService
import com.example.domain.entity.Course
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class CourseRepositoryImplTest {
    private lateinit var database: CoursesDatabase
    private lateinit var repository: CourseRepositoryImpl

    private val fakeApiService = object : CourseApiService {
        override suspend fun getCourses(): CoursesResponse = CoursesResponse(emptyList())
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CoursesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = CourseRepositoryImpl(fakeApiService, database.coursesDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getFavoritesCourses_returns_an_empty_Flow_for_an_empty_database() = runTest {
        val result = repository.getFavoritesCourses()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.first().isEmpty())
    }

    @Test
    fun getFavoritesCourses_reacts_to_course_addition() = runTest {
        val course = createCourse()
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getFavoritesCourses().getOrNull()!!.first()

        assertEquals(result.size, 1)
        assertEquals(result.first().id, course.id)
    }

    @Test
    fun getFavoritesCourses_responds_to_course_deletion() = runTest {
        val course = createCourse()
        repository.toggleCourseFavoriteStatus(course)
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getFavoritesCourses().getOrNull()!!.first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun toggleCourseFavoriteStatus_adds_the_course_if_it_is_not_in_the_database() = runTest {
        val course = createCourse()

        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(course.id)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun toggleCourseFavoriteStatus_deletes_the_course_if_it_exists_in_the_database() = runTest {
        val course = createCourse()
        repository.toggleCourseFavoriteStatus(course)

        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(course.id)
        assertNull(result.getOrNull())
    }

    @Test
    fun toggleCourseFavoriteStatus_returns_Result_success() = runTest {
        val course = createCourse()

        val result = repository.toggleCourseFavoriteStatus(course)

        assertTrue(result.isSuccess)
    }

    @Test
    fun getCourseFromFavorites_returns_course_by_id() = runTest {
        val course = createCourse()
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(1)

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.id, 1)
    }

    @Test
    fun getCourseFromFavorites_returns_null_if_cursor_not_found() = runTest {
        val result = repository.getCourseFromFavorites(999)

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    private fun createCourse() = Course(
        id = 1,
        title = "Title",
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = false,
        publishDate = LocalDate.now()
    )
}
