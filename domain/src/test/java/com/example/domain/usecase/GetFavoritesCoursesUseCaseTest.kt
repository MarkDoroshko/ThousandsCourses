package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@DisplayName("GetFavoritesCoursesUseCase")
class GetFavoritesCoursesUseCaseTest {
    private val repository: CourseRepository = mock()
    private lateinit var useCase: GetFavoritesCoursesUseCase

    @BeforeEach
    fun setUp() {
        useCase = GetFavoritesCoursesUseCase(repository)
    }

    @Test
    @DisplayName("Возвращает Result.success с Flow избранных курсов")
    fun returnSuccessWithFavoritesFlow() = runTest {
        val courses = listOf(createCourse())
        val flow = flowOf(courses)

        whenever(repository.getFavoritesCourses()).thenReturn(Result.success(flow))

        val result = useCase()

        assertTrue { result.isSuccess }
        assertNotNull(result.getOrNull())
    }

    @Test
    @DisplayName("Возвращает Result.failure при ошибке репозитория")
    fun returnFailureWhenRepositoryFails() = runTest {
        val exception = RuntimeException("Database error")

        whenever(repository.getFavoritesCourses()).thenReturn(Result.failure(exception))

        val result = useCase()

        assertTrue { result.isFailure }
    }

    @Test
    @DisplayName("Делегирует вызов в репозиторий")
    fun delegatesToRepository() = runTest {
        whenever(repository.getFavoritesCourses()).thenReturn(Result.success(flowOf()))

        useCase()

        verify(repository).getFavoritesCourses()
    }

    private fun createCourse() = Course(
        id = 1,
        title = "Title",
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = true,
        publishDate = LocalDate.now()
    )
}
