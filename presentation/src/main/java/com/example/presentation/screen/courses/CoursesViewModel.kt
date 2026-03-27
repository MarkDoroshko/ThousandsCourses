package com.example.presentation.screen.courses

import androidx.lifecycle.ViewModel
import com.example.domain.entity.Course
import com.example.domain.usecase.GetCoursesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CoursesState())
    val state = _state.asStateFlow()

    init {
        loadCourses()
    }

    private fun loadCourses() {
        _state.value = _state.value.copy(isLoading = true)

        viewModel
        getCoursesUseCase().fold(
            onSuccess = {
                _state.value = _state.value.copy(courses = it, isLoading = false)
            },
            onFailure = {
                _state.value = _state.value.copy(isLoading = false, error = it.message)
            }
        )
    }
}

data class CoursesState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)