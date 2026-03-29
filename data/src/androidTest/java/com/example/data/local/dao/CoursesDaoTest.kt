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
    fun addCourse_сохраняет_запись_в_базу() = runTest {
        val course = createCourseDbModel(id = 1)

        dao.addCourse(course)

        val result = dao.getCourse(1)
        assertEquals(result, course)
    }

    @Test
    fun addCourse_повторная_вставка_заменяет_существующую() = runTest {
        val original = createCourseDbModel(id = 1, title = "Original")
        val updated = createCourseDbModel(id = 1, title = "Updated")

        dao.addCourse(original)
        dao.addCourse(updated)

        val result = dao.getCourse(1)
        assertEquals(result?.title, "Updated")
    }

    @Test
    fun deleteCourse_удаляет_запись_по_id() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        dao.deleteCourse(1)

        val result = dao.getCourse(1)
        assertNull(result)
    }

    @Test
    fun deleteCourse_несуществующей_записи_не_вызывает_ошибки() = runTest {
        dao.deleteCourse(999)

        val result = dao.getCourse(999)
        assertNull(result)
    }

    @Test
    fun getCourses_возвращает_пустой_список_для_пустой_базы() = runTest {
        val result = dao.getCourses().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun getCourses_обновляется_при_добавлении_курса() = runTest {
        val course = createCourseDbModel(id = 1)

        dao.addCourse(course)

        val result = dao.getCourses().first()
        assertEquals(result.size, 1)
        assertEquals(result.first(), course)
    }

    @Test
    fun getCourses_обновляется_при_удалении_курса() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        dao.deleteCourse(1)

        val result = dao.getCourses().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getCourse_возвращает_запись_по_id() = runTest {
        val course = createCourseDbModel(id = 1)
        dao.addCourse(course)

        val result = dao.getCourse(1)

        assertEquals(result, course)
    }

    @Test
    fun getCourse_возвращает_null_если_запись_не_найдена() = runTest {
        val result = dao.getCourse(999)

        assertNull(result)
    }

    @Test
    fun addCourse_корректно_сохраняет_и_читает_LocalDate() = runTest {
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
