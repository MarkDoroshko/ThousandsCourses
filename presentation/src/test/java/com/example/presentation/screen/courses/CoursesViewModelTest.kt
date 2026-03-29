@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.screen.courses

import com.example.domain.entity.Course
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.GetFavoritesCoursesUseCase
import com.example.domain.usecase.ToggleCourseFavoriteStatusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

@DisplayName("CoursesViewModel")
class CoursesViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private val getCoursesUseCase: GetCoursesUseCase = mock()
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
        whenever(getCoursesUseCase()).thenReturn(Result.success(emptyList()))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()

        assertTrue { viewModel.state.value.isLoading }
    }

    @Test
    @DisplayName("После успешной загрузки сбрасывает isLoading")
    fun isLoadingFalseAfterSuccessfulLoad() = runTest(testDispatcher) {
        whenever(getCoursesUseCase()).thenReturn(Result.success(emptyList()))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse { viewModel.state.value.isLoading }
    }

    @Test
    @DisplayName("После успешной загрузки обновляет список курсов")
    fun coursesUpdatedAfterSuccessfulLoad() = runTest(testDispatcher) {
        val courses = listOf(createCourse(id = 1))

        whenever(getCoursesUseCase()).thenReturn(Result.success(courses))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(viewModel.state.value.courses, courses)
    }

    @Test
    @DisplayName("После успешной загрузки курсы отсортированы по возрастанию даты публикации")
    fun coursesAreSortedByPublishDateAscAfterLoad() = runTest(testDispatcher) {
        val older = createCourse(id = 1, publishDate = LocalDate.of(2024, 1, 1))
        val newer = createCourse(id = 2, publishDate = LocalDate.of(2024, 6, 1))

        whenever(getCoursesUseCase()).thenReturn(Result.success(listOf(newer, older)))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(viewModel.state.value.courses, listOf(older, newer))
    }

    @Test
    @DisplayName("После ошибки при загрузке курсов устанавливает error")
    fun errorSetAfterFailedCoursesLoad() = runTest(testDispatcher) {
        val exception = RuntimeException("Network error")

        whenever(getCoursesUseCase()).thenReturn(Result.failure(exception))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(viewModel.state.value.error, "Network error")
    }

    @Test
    @DisplayName("SortedCourses меняет тип сортировки с NON_DECREASING на DECREASING")
    fun sortedCoursesChangesTypeSortedToDecreasing() = runTest(testDispatcher) {
        whenever(getCoursesUseCase()).thenReturn(Result.success(emptyList()))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.SortedCourses)

        assertEquals(viewModel.state.value.typeSorted, TypeSorted.DECREASING)
    }

    @Test
    @DisplayName("SortedCourses дважды возвращает тип сортировки к NON_DECREASING")
    fun sortedCoursesTwiceReturnsToNonDecreasing() = runTest(testDispatcher) {
        whenever(getCoursesUseCase()).thenReturn(Result.success(emptyList()))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.SortedCourses)
        viewModel.processIntent(CoursesIntent.SortedCourses)

        assertEquals(viewModel.state.value.typeSorted, TypeSorted.NON_DECREASING)
    }

    @Test
    @DisplayName("SortedCourses сортирует курсы по убыванию даты публикации")
    fun sortedCoursesSortsByDescendingPublishDate() = runTest(testDispatcher) {
        val older = createCourse(id = 1, publishDate = LocalDate.of(2024, 1, 1))
        val newer = createCourse(id = 2, publishDate = LocalDate.of(2024, 6, 1))

        whenever(getCoursesUseCase()).thenReturn(Result.success(listOf(older, newer)))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.SortedCourses)

        assertEquals(viewModel.state.value.courses, listOf(newer, older))
    }

    @Test
    @DisplayName("ToggleFavoriteStatus добавляет курс в избранное если его нет в списке")
    fun toggleFavoriteStatusAddsCourseWhenNotInFavorites() = runTest(testDispatcher) {
        val course = createCourse(id = 1)
        val favoritesFlow = MutableStateFlow<List<Course>>(emptyList())

        whenever(getCoursesUseCase()).thenReturn(Result.success(listOf(course)))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(favoritesFlow))
        whenever(toggleCourseFavoriteStatusUseCase(course)).thenReturn(Result.success(Unit))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.ToggleFavoriteStatus(course))
        favoritesFlow.value = listOf(course)
        advanceUntilIdle()

        assertTrue { viewModel.state.value.favorites.any { it.id == course.id } }
    }

    @Test
    @DisplayName("ToggleFavoriteStatus удаляет курс из избранного если он есть в списке")
    fun toggleFavoriteStatusRemovesCourseWhenInFavorites() = runTest(testDispatcher) {
        val course = createCourse(id = 1)
        val favoritesFlow = MutableStateFlow<List<Course>>(listOf(course))

        whenever(getCoursesUseCase()).thenReturn(Result.success(listOf(course)))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(favoritesFlow))
        whenever(toggleCourseFavoriteStatusUseCase(course)).thenReturn(Result.success(Unit))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.ToggleFavoriteStatus(course))
        favoritesFlow.value = emptyList()
        advanceUntilIdle()

        assertFalse { viewModel.state.value.favorites.any { it.id == course.id } }
    }

    @Test
    @DisplayName("ToggleFavoriteStatus при ошибке устанавливает error")
    fun toggleFavoriteStatusSetsErrorOnFailure() = runTest(testDispatcher) {
        val course = createCourse(id = 1)
        val exception = RuntimeException("DB error")

        whenever(getCoursesUseCase()).thenReturn(Result.success(listOf(course)))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))
        whenever(toggleCourseFavoriteStatusUseCase(course)).thenReturn(Result.failure(exception))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.ToggleFavoriteStatus(course))
        advanceUntilIdle()

        assertEquals(viewModel.state.value.error, "DB error")
    }

    @Test
    @DisplayName("DismissError обнуляет error")
    fun dismissErrorClearsError() = runTest(testDispatcher) {
        val exception = RuntimeException("Network error")

        whenever(getCoursesUseCase()).thenReturn(Result.failure(exception))
        whenever(getFavoritesCoursesUseCase()).thenReturn(Result.success(flowOf(emptyList())))

        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.processIntent(CoursesIntent.DismissError)

        assertNull(viewModel.state.value.error)
    }

    private fun createViewModel() = CoursesViewModel(
        getCoursesUseCase,
        getFavoritesCoursesUseCase,
        toggleCourseFavoriteStatusUseCase
    )

    private fun createCourse(id: Int, publishDate: LocalDate = LocalDate.now()) = Course(
        id = id,
        title = "Title",
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = false,
        publishDate = publishDate
    )
}
