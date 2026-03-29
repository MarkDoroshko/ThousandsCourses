package com.example.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Course
import com.example.domain.usecase.GetFavoritesCoursesUseCase
import com.example.domain.usecase.ToggleCourseFavoriteStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesCoursesUseCase: GetFavoritesCoursesUseCase,
    private val toggleCourseFavoriteStatusUseCase: ToggleCourseFavoriteStatusUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            getFavoritesCoursesUseCase().fold(
                onSuccess = { flow ->
                    flow.collect { favorites ->
                        _state.update { it.copy(favorites = favorites, isLoading = false) }
                    }
                },
                onFailure = {
                    _state.value = _state.value.copy(isLoading = false, error = it.message)
                }
            )
        }
    }

    fun processIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.DismissError -> {
                _state.update { it.copy(error = null) }
            }

            is FavoritesIntent.ToggleFavoriteStatus -> toggleFavoriteStatus(intent.course)
        }
    }

    private fun toggleFavoriteStatus(course: Course) {
        viewModelScope.launch {
            toggleCourseFavoriteStatusUseCase(course).fold(
                onSuccess = {},
                onFailure = {
                    _state.value = _state.value.copy(error = it.message)
                }
            )

            _state.update { previousState ->
                previousState.copy(
                    favorites = previousState.favorites.filter { it.id != course.id }
                )
            }
        }
    }
}

data class FavoritesState(
    val favorites: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface FavoritesIntent {
    data class ToggleFavoriteStatus(
        val course: Course
    ) : FavoritesIntent

    data object DismissError : FavoritesIntent
}