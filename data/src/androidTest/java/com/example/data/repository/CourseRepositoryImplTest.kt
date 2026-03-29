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
    fun getFavoritesCourses_возвращает_пустой_Flow_для_пустой_базы() = runTest {
        val result = repository.getFavoritesCourses()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.first().isEmpty())
    }

    @Test
    fun getFavoritesCourses_реагирует_на_добавление_курса() = runTest {
        val course = createCourse(id = 1)
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getFavoritesCourses().getOrNull()!!.first()

        assertEquals(result.size, 1)
        assertEquals(result.first().id, course.id)
    }

    @Test
    fun getFavoritesCourses_реагирует_на_удаление_курса() = runTest {
        val course = createCourse(id = 1)
        repository.toggleCourseFavoriteStatus(course)
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getFavoritesCourses().getOrNull()!!.first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun toggleCourseFavoriteStatus_добавляет_курс_если_его_нет_в_базе() = runTest {
        val course = createCourse(id = 1)

        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(course.id)
        assertNotNull(result.getOrNull())
    }

    @Test
    fun toggleCourseFavoriteStatus_удаляет_курс_если_он_есть_в_базе() = runTest {
        val course = createCourse(id = 1)
        repository.toggleCourseFavoriteStatus(course)

        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(course.id)
        assertNull(result.getOrNull())
    }

    @Test
    fun toggleCourseFavoriteStatus_возвращает_Result_success() = runTest {
        val course = createCourse(id = 1)

        val result = repository.toggleCourseFavoriteStatus(course)

        assertTrue(result.isSuccess)
    }

    @Test
    fun getCourseFromFavorites_возвращает_курс_по_id() = runTest {
        val course = createCourse(id = 1)
        repository.toggleCourseFavoriteStatus(course)

        val result = repository.getCourseFromFavorites(1)

        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.id, 1)
    }

    @Test
    fun getCourseFromFavorites_возвращает_null_если_курс_не_найден() = runTest {
        val result = repository.getCourseFromFavorites(999)

        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    private fun createCourse(id: Int = 1) = Course(
        id = id,
        title = "Title",
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = false,
        publishDate = LocalDate.now()
    )
}
