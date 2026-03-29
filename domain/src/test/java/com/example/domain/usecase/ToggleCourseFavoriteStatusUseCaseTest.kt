package com.example.domain.usecase

import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@DisplayName("ToggleCourseFavoriteStatusUseCase")
class ToggleCourseFavoriteStatusUseCaseTest {
    private val repository: CourseRepository = mock()
    private lateinit var useCase: ToggleCourseFavoriteStatusUseCase

    @BeforeEach
    fun setUp() {
        useCase = ToggleCourseFavoriteStatusUseCase(repository)
    }

    @Test
    @DisplayName("Возвращает Result.success при успешном переключении")
    fun returnSuccessOnToggle() = runTest {
        val course = createCourse()

        whenever(repository.toggleCourseFavoriteStatus(course)).thenReturn(Result.success(Unit))

        val result = useCase(course)

        assertTrue { result.isSuccess }
    }

    @Test
    @DisplayName("Возвращает Result.failure при ошибке репозитория")
    fun returnFailureWhenRepositoryFails() = runTest {
        val course = createCourse()
        val exception = RuntimeException("Database error")

        whenever(repository.toggleCourseFavoriteStatus(course)).thenReturn(Result.failure(exception))

        val result = useCase(course)

        assertTrue { result.isFailure }
    }

    @Test
    @DisplayName("Делегирует вызов в репозиторий с переданным курсом")
    fun delegatesToRepositoryWithCourse() = runTest {
        val course = createCourse()

        whenever(repository.toggleCourseFavoriteStatus(course)).thenReturn(Result.success(Unit))

        useCase(course)

        verify(repository).toggleCourseFavoriteStatus(course)
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
