package com.example.data.local.mapper

import com.example.data.local.model.CourseDbModel
import com.example.domain.entity.Course
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("LocalCourseMapper")
class LocalCourseMapperTest {

    @Test
    @DisplayName("Course.toDbModel() корректно маппирует все поля")
    fun courseToDbModelMapsAllFields() {
        val course = createCourse()

        val result = course.toDbModel()

        assertEquals(result.id, course.id)
        assertEquals(result.title, course.title)
        assertEquals(result.text, course.text)
        assertEquals(result.price, course.price)
        assertEquals(result.rate, course.rate)
        assertEquals(result.startDate, course.startDate)
        assertEquals(result.hasLike, course.hasLike)
        assertEquals(result.publishDate, course.publishDate)
    }

    @Test
    @DisplayName("CourseDbModel.toEntity() корректно маппирует все поля")
    fun courseDbModelToEntityMapsAllFields() {
        val dbModel = createCourseDbModel()

        val result = dbModel.toEntity()

        assertEquals(result.id, dbModel.id)
        assertEquals(result.title, dbModel.title)
        assertEquals(result.text, dbModel.text)
        assertEquals(result.price, dbModel.price)
        assertEquals(result.rate, dbModel.rate)
        assertEquals(result.startDate, dbModel.startDate)
        assertEquals(result.hasLike, dbModel.hasLike)
        assertEquals(result.publishDate, dbModel.publishDate)
    }

    @Test
    @DisplayName("List<CourseDbModel>.toEntities() возвращает список правильного размера")
    fun dbModelListToEntitiesReturnsCorrectSize() {
        val dbModels = listOf(createCourseDbModel(id = 1), createCourseDbModel(id = 2))

        val result = dbModels.toEntities()

        assertEquals(result.size, 2)
    }

    @Test
    @DisplayName("List<CourseDbModel>.toEntities() для пустого списка возвращает пустой список")
    fun emptyDbModelListToEntitiesReturnsEmptyList() {
        val result = emptyList<CourseDbModel>().toEntities()

        assertTrue { result.isEmpty() }
    }

    @Test
    @DisplayName("List<CourseDbModel>.toEntities() корректно маппирует каждый элемент")
    fun dbModelListToEntitiesMapsEachElement() {
        val dbModels = listOf(
            createCourseDbModel(id = 1, title = "Course 1"),
            createCourseDbModel(id = 2, title = "Course 2")
        )

        val result = dbModels.toEntities()

        assertEquals(result[0].id, 1)
        assertEquals(result[0].title, "Course 1")
        assertEquals(result[1].id, 2)
        assertEquals(result[1].title, "Course 2")
    }

    private fun createCourse() = Course(
        id = 1,
        title = "Test Course",
        text = "Description",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.of(2025, 1, 15),
        hasLike = false,
        publishDate = LocalDate.of(2024, 6, 1)
    )

    private fun createCourseDbModel(
        id: Int = 1,
        title: String = "Test Course"
    ) = CourseDbModel(
        id = id,
        title = title,
        text = "Description",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.of(2025, 1, 15),
        hasLike = false,
        publishDate = LocalDate.of(2024, 6, 1)
    )
}
