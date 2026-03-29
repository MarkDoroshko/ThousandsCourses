package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@DisplayName("GetCoursesUseCase")
class GetCoursesUseCaseTest {
    private val repository: CourseRepository = mock()
    private lateinit var useCase: GetCoursesUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetCoursesUseCase(repository)
    }

    @Test
    @DisplayName("Возвращает Result.success со списком курсов")
    fun returnSuccessWithCourses() = runTest {
        val courses = listOf(createCourse(hasLike = false))

        whenever(repository.getCourses()).thenReturn(Result.success(courses))

        val result = useCase()

        assertTrue { result.isSuccess }
        assertEquals(result.getOrNull(), courses)
    }

    @Test
    @DisplayName("Возвращает Result.failure при ошибке репозитория")
    fun returnFailureWhenRepositoryFails() = runTest {
        val exception = RuntimeException("Network error")

        whenever(repository.getCourses()).thenReturn(Result.failure(exception))

        val result = useCase()

        assertTrue { result.isFailure }
    }

    @Test
    @DisplayName("Для курса с hasLike=false не вызывает getCourseFromFavorites")
    fun notCallGetCourseFromFavoritesWhenHasLikeFalse() = runTest {
        val course = createCourse(hasLike = false)

        whenever(repository.getCourses()).thenReturn(Result.success(listOf(course)))

        useCase()

        verify(repository, never()).getCourseFromFavorites(course.id)
    }

    @Test
    @DisplayName("Для курса с hasLike=true, которого нет в избранном, вызывает toggleCourseFavoriteStatus")
    fun toggleFavoriteWhenCourseNotInFavorites() = runTest {
        val course = createCourse(hasLike = true)

        whenever(repository.getCourses()).thenReturn(Result.success(listOf(course)))
        whenever(repository.getCourseFromFavorites(course.id)).thenReturn(Result.success(null))
        whenever(repository.toggleCourseFavoriteStatus(course)).thenReturn(Result.success(Unit))

        useCase()

        verify(repository).toggleCourseFavoriteStatus(course)
    }

    @Test
    @DisplayName("Для курса с hasLike=true, который уже есть в избранном, не вызывает toggleCourseFavoriteStatus")
    fun notToggleFavoriteWhenCourseAlreadyInFavorites() = runTest {
        val course = createCourse(hasLike = true)

        whenever(repository.getCourses()).thenReturn(Result.success(listOf(course)))
        whenever(repository.getCourseFromFavorites(course.id)).thenReturn(Result.success(course))

        useCase()

        verify(repository, never()).toggleCourseFavoriteStatus(course)
    }

    private fun createCourse(hasLike: Boolean) = Course(
        id = 1,
        title = "Title",
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = hasLike,
        publishDate = LocalDate.now()
    )
}
