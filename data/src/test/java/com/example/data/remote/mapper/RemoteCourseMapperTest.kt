package com.example.data.remote.mapper

import com.example.data.remote.dto.CourseDto
import com.example.data.remote.dto.CoursesResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("RemoteCourseMapper")
class RemoteCourseMapperTest {

    @Test
    @DisplayName("CourseDto.toEntity() корректно маппирует все поля")
    fun courseDtoToEntityMapsAllFields() {
        val dto = createCourseDto()

        val result = dto.toEntity()

        assertEquals(result.id, dto.id)
        assertEquals(result.title, dto.title)
        assertEquals(result.text, dto.text)
        assertEquals(result.hasLike, dto.hasLike)
    }

    @Test
    @DisplayName("CourseDto.toEntity() парсит price строку с пробелами в Int")
    fun courseDtoToEntityParsesPriceWithSpaces() {
        val dto = createCourseDto(price = "1 000")

        val result = dto.toEntity()

        assertEquals(result.price, 1000)
    }

    @Test
    @DisplayName("CourseDto.toEntity() парсит rate строку в Double")
    fun courseDtoToEntityParsesRate() {
        val dto = createCourseDto(rate = "4.5")

        val result = dto.toEntity()

        assertEquals(result.rate, 4.5)
    }

    @Test
    @DisplayName("CourseDto.toEntity() парсит startDate строку в LocalDate")
    fun courseDtoToEntityParsesStartDate() {
        val dto = createCourseDto(startDate = "2025-01-15")

        val result = dto.toEntity()

        assertEquals(result.startDate, LocalDate.of(2025, 1, 15))
    }

    @Test
    @DisplayName("CourseDto.toEntity() парсит publishDate строку в LocalDate")
    fun courseDtoToEntityParsesPublishDate() {
        val dto = createCourseDto(publishDate = "2024-06-01")

        val result = dto.toEntity()

        assertEquals(result.publishDate, LocalDate.of(2024, 6, 1))
    }

    @Test
    @DisplayName("CoursesResponse.toListCourse() возвращает список правильного размера")
    fun coursesResponseToListCourseReturnsCorrectSize() {
        val response = CoursesResponse(
            courses = listOf(createCourseDto(id = 1), createCourseDto(id = 2))
        )

        val result = response.toListCourse()

        assertEquals(result.size, 2)
    }

    @Test
    @DisplayName("CoursesResponse.toListCourse() для пустого списка возвращает пустой список")
    fun coursesResponseToListCourseReturnsEmptyList() {
        val response = CoursesResponse(courses = emptyList())

        val result = response.toListCourse()

        assertTrue { result.isEmpty() }
    }

    @Test
    @DisplayName("CoursesResponse.toListCourse() корректно маппирует каждый элемент")
    fun coursesResponseToListCourseMapsEachElement() {
        val response = CoursesResponse(
            courses = listOf(createCourseDto(id = 1, title = "Course 1"), createCourseDto(id = 2, title = "Course 2"))
        )

        val result = response.toListCourse()

        assertEquals(result[0].id, 1)
        assertEquals(result[0].title, "Course 1")
        assertEquals(result[1].id, 2)
        assertEquals(result[1].title, "Course 2")
    }

    private fun createCourseDto(
        id: Int = 1,
        title: String = "Test Course",
        text: String = "Description",
        price: String = "1 000",
        rate: String = "4.5",
        startDate: String = "2025-01-15",
        hasLike: Boolean = false,
        publishDate: String = "2024-06-01"
    ) = CourseDto(
        id = id,
        title = title,
        text = text,
        price = price,
        rate = rate,
        startDate = startDate,
        hasLike = hasLike,
        publishDate = publishDate
    )
}
