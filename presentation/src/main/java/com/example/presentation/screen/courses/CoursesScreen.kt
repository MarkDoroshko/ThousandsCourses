package com.example.presentation.screen.courses

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.component.AppErrorSnackbar
import com.example.presentation.component.AppTextField
import com.example.presentation.component.CourseItem
import com.example.presentation.component.Loader

@Composable
fun CoursesScreen(
    modifier: Modifier = Modifier,
    viewModel: CoursesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.typeSorted) {
        listState.scrollToItem(0)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppTextField(
                    modifier = Modifier.weight(2f),
                    value = state.query,
                    onValueChange = { viewModel.processIntent(CoursesIntent.SearchQueryChanged(it)) },
                    placeholderText = stringResource(R.string.search_courses),
                    leadingIcon = painterResource(R.drawable.ic_search),
                    height = 56.dp,
                    paddingStart = 16.dp,
                    color = MaterialTheme.colorScheme.surface,
                    radius = 28.dp
                )

                Button(
                    modifier = Modifier.size(56.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .width(16.dp)
                            .height(20.dp),
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { viewModel.processIntent(CoursesIntent.SortedCourses) },
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.sorting),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    modifier = Modifier
                        .width(13.34.dp)
                        .height(11.21.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(R.drawable.ic_sorting),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Loader()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.courses, key = { course -> course.id }) { course ->
                        CourseItem(
                            course = course,
                            isFavorite = state.favorites.any { it.id == course.id },
                            onToggleFavorite = {
                                viewModel.processIntent(CoursesIntent.ToggleFavoriteStatus(course))
                            }
                        )
                    }
                }
            }
        }

        AppErrorSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            errorMessage = state.error,
            onDismiss = { viewModel.processIntent(CoursesIntent.DismissError) }
        )
    }
}