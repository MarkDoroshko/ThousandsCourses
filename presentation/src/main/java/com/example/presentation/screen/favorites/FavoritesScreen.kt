package com.example.presentation.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.presentation.component.CourseItem

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        items(state.favorites) {
            CourseItem(
                course = it,
                isFavorite = state.favorites.any { favoriteCourse -> favoriteCourse.id == it.id },
                onToggleFavorite = { viewModel.processIntent(FavoritesIntent.ToggleFavoriteStatus(it)) }
            )
        }
    }
}