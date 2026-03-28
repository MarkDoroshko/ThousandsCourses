package com.example.presentation.screen.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Course
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.GetFavoritesCoursesUseCase
import com.example.domain.usecase.ToggleCourseFavoriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val getFavoritesCoursesUseCase: GetFavoritesCoursesUseCase,
    private val toggleCourseFavoriteStatusUseCase: ToggleCourseFavoriteStatusUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CoursesState())
    val state = _state.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            getCoursesUseCase().fold(
                onSuccess = {
                    val sorted = it.sortedBy { course -> course.publishDate }
                    _state.value = _state.value.copy(courses = sorted, isLoading = false)
                },
                onFailure = {
                    _state.value = _state.value.copy(isLoading = false, error = it.message)
                }
            )
            getFavoritesCoursesUseCase().fold(
                onSuccess = { flow ->
                    flow.collect { favorites ->
                        _state.update { it.copy(favorites = favorites) }
                    }
                },
                onFailure = {
                    _state.value = _state.value.copy(error = it.message)
                }
            )
        }
    }

    fun processIntent(intent: CoursesIntent) {
        when (intent) {
            CoursesIntent.DismissError -> {
                _state.update { it.copy(error = null) }
            }

            CoursesIntent.SortedCourses -> {
                val newType = when (_state.value.typeSorted) {
                    TypeSorted.NON_DECREASING -> TypeSorted.DECREASING
                    TypeSorted.DECREASING -> TypeSorted.NON_DECREASING
                }
                val sorted = when (newType) {
                    TypeSorted.NON_DECREASING -> _state.value.courses.sortedBy { it.publishDate }
                    TypeSorted.DECREASING -> _state.value.courses.sortedByDescending { it.publishDate }
                }
                _state.update { it.copy(courses = sorted, typeSorted = newType) }
            }

            is CoursesIntent.ToggleFavoriteStatus -> toggleFavoriteStatus(intent.course)
        }
    }

    private fun toggleFavoriteStatus(course: Course) {
        viewModelScope.launch {
            toggleCourseFavoriteStatusUseCase(course).fold(
                onSuccess = {
                    val updatedFavorites = if (_state.value.favorites.any { it.id == course.id }) {
                        _state.value.favorites.filter { it.id != course.id }
                    } else {
                        _state.value.favorites + course
                    }
                    _state.update { it.copy(favorites = updatedFavorites) }
                },
                onFailure = {
                    _state.value = _state.value.copy(error = it.message)
                }
            )
        }
    }
}

data class CoursesState(
    val courses: List<Course> = emptyList(),
    val favorites: List<Course> = emptyList(),

    val typeSorted: TypeSorted = TypeSorted.NON_DECREASING,

    val query: String = "",

    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface CoursesIntent {
    data object SortedCourses : CoursesIntent

    data class ToggleFavoriteStatus(
        val course: Course
    ) : CoursesIntent

    data object DismissError : CoursesIntent
}

enum class TypeSorted { DECREASING, NON_DECREASING }