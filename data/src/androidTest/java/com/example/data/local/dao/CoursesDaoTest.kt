package com.example.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.local.database.CoursesDatabase
import com.example.data.local.model.CourseDbModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class CoursesDaoTest {
    private lateinit var database: CoursesDatabase
    private lateinit var dao: CoursesDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CoursesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.coursesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun addCourse_saves_the_record_to_the_database() = runTest {
        val course = createCourseDbModel(id = 1)

        dao.addCourse(course)

        val result = dao.getCourse(1)
        assertEquals(result, course)
    }

    @Test
    fun addCourse_reinserting_replaces_existing_one() = runTest {
        val original = createCourseDbModel(id = 1, title = "Original")
        val updated = createCourseDbModel(id = 1, title = "Updated")

        dao.addCourse(original)
        dao.addCourse(updated)

        val result = dao.getCourse(1)
        assertEquals(result?.title, "Updated")
    }

    @Test
    fun deleteCourse_delete_record_by_id() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        dao.deleteCourse(1)

        val result = dao.getCourse(1)
        assertNull(result)
    }

    @Test
    fun deleteCourse_non_existent_record_does_not_cause_an_error() = runTest {
        dao.deleteCourse(999)

        val result = dao.getCourse(999)
        assertNull(result)
    }

    @Test
    fun getCourses_returns_an_empty_list_for_an_empty_database() = runTest {
        val result = dao.getCourses().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getCourses_updated_when_a_course_is_added() = runTest {
        val course = createCourseDbModel(id = 1)

        dao.addCourse(course)

        val result = dao.getCourses().first()
        assertEquals(result.size, 1)
        assertEquals(result.first(), course)
    }

    @Test
    fun getCourses_updated_when_a_course_is_deleted() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        dao.deleteCourse(1)

        val result = dao.getCourses().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getCourse_returns_record_by_id() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        val result = dao.getCourse(1)

        assertEquals(result, course)
    }

    @Test
    fun getCourse_returns_null_if_record_not_found() = runTest {
        val result = dao.getCourse(999)

        assertNull(result)
    }

    @Test
    fun addCourse_correctly_saves_and_reads_LocalDate() = runTest {
        val startDate = LocalDate.of(2024, 3, 15)
        val publishDate = LocalDate.of(2024, 6, 1)
        val course = createCourseDbModel(id = 1, startDate = startDate, publishDate = publishDate)

        dao.addCourse(course)

        val result = dao.getCourse(1)
        assertEquals(result?.startDate, startDate)
        assertEquals(result?.publishDate, publishDate)
    }

    private fun createCourseDbModel(
        id: Int = 1,
        title: String = "Title",
        startDate: LocalDate = LocalDate.now(),
        publishDate: LocalDate = LocalDate.now()
    ) = CourseDbModel(
        id = id,
        title = title,
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = startDate,
        hasLike = false,
        publishDate = publishDate
    )
}
