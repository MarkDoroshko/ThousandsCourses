@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.screen.favorites

import com.example.domain.entity.Course
import com.example.domain.usecase.GetFavoritesCoursesUseCase
import com.example.domain.usecase.ToggleCourseFavoriteStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate

@DisplayName("FavoritesViewModel")
class FavoritesViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val getFavoritesCoursesUseCase: GetFavoritesCoursesUseCase = mock()
    private val toggleCourseFavoriteStatusUseCase: ToggleCourseFavoriteStatusUseCase = mock()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("При инициализации устанавливает isLoading = true")
    fun isLoadingTrueOnInit() = runTest(testDispatcher) {
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()

        assertTrue { viewModel.state.value.isLoading }
    }

    @Test
    @DisplayName("После успешной загрузки обновляет список избранных")
    fun favoritesUpdatedAfterSuccessfulLoad() = runTest(testDispatcher) {
        val courses = listOf(createCourse())

        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(courses)))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(viewModel.state.value.favorites, courses)
    }

    @Test
    @DisplayName("После успешной загрузки сбрасывает isLoading")
    fun isLoadingFalseAfterSuccessfulLoad() = runTest(testDispatcher) {
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse { viewModel.state.value.isLoading }
    }

    @Test
    @DisplayName("После ошибки при загрузке устанавливает error")
    fun errorSetAfterFailedLoad() = runTest(testDispatcher) {
        val exception = RuntimeException("DB error")

        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.failure(exception))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(viewModel.state.value.error, "DB error")
    }

    @Test
    @DisplayName("ToggleFavoriteStatus удаляет курс из списка избранных")
    fun toggleFavoriteStatusRemovesCourseFromFavorites() = runTest(testDispatcher) {
        val course = createCourse()

        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(listOf(course))))
        whenever(toggleCourseFavoriteStatusUseCase(course)).thenReturn(Result.success(Unit))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(FavoritesIntent.ToggleFavoriteStatus(course))
        advanceUntilIdle()

        assertFalse { viewModel.state.value.favorites.any { it.id == course.id } }
    }

    @Test
    @DisplayName("ToggleFavoriteStatus при ошибке устанавливает error")
    fun toggleFavoriteStatusSetsErrorOnFailure() = runTest(testDispatcher) {
        val course = createCourse()
        val exception = RuntimeException("DB error")

        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(listOf(course))))
        whenever(toggleCourseFavoriteStatusUseCase(course)).thenReturn(Result.failure(exception))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(FavoritesIntent.ToggleFavoriteStatus(course))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.error, "DB error")
    }

    @Test
    @DisplayName("DismissError обнуляет error")
    fun dismissErrorClearsError() = runTest(testDispatcher) {
        val exception = RuntimeException("DB error")

        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.failure(exception))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(FavoritesIntent.DismissError)

        assertNull(viewModel.state.value.error)
    }

    private fun createViewModel() = FavoritesViewModel(
        getFavoritesCoursesUseCase,
        toggleCourseFavoriteStatusUseCase
    )

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
