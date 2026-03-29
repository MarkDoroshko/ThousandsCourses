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
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getCoursesUseCase().fold(
                onSuccess = { courses ->
                    _state.update { previousState ->
                        previousState.copy(
                            courses = courses.sortedBy { it.publishDate },
                            isLoading = false
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update { it.copy(isLoading = false, error = throwable.message) }
                }
            )
        }

        viewModelScope.launch {
            getFavoritesCoursesUseCase().fold(
                onSuccess = { flow ->
                    flow.collect { favorites ->
                        _state.update { it.copy(favorites = favorites) }
                    }
                },
                onFailure = { throwable ->
                    _state.update { it.copy(error = throwable.message) }
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
                _state.update { previousState ->
                    val newType = when (previousState.typeSorted) {
                        TypeSorted.NON_DECREASING -> TypeSorted.DECREASING
                        TypeSorted.DECREASING -> TypeSorted.NON_DECREASING
                    }

                    val sorted = when (newType) {
                        TypeSorted.NON_DECREASING -> previousState.courses.sortedBy { it.publishDate }
                        TypeSorted.DECREASING -> previousState.courses.sortedByDescending { it.publishDate }
                    }

                    previousState.copy(courses = sorted, typeSorted = newType)
                }
            }

            is CoursesIntent.ToggleFavoriteStatus -> toggleFavoriteStatus(intent.course)

            is CoursesIntent.SearchQueryChanged -> {}
        }
    }

    private fun toggleFavoriteStatus(course: Course) {
        viewModelScope.launch {
            toggleCourseFavoriteStatusUseCase(course).fold(
                onSuccess = {},
                onFailure = { throwable ->
                    _state.update { it.copy(error = throwable.message) }
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

    data class SearchQueryChanged(val query: String) : CoursesIntent
}

enum class TypeSorted { DECREASING, NON_DECREASING }
