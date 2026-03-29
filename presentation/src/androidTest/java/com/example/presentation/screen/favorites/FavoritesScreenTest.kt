@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.screen.favorites

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import com.example.domain.usecase.GetFavoritesCoursesUseCase
import com.example.domain.usecase.ToggleCourseFavoriteStatusUseCase
import com.example.presentation.theme.ThousandsCoursesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class FavoritesScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun favoritesScreen_empty_list_in_absence_of_elected() {
        val viewModel = createViewModel(favorites = emptyList())

        rule.setContent {
            ThousandsCoursesTheme {
                FavoritesScreen(viewModel = viewModel)
            }
        }

        rule.onAllNodes(hasClickAction()).assertCountEquals(0)
    }

    @Test
    fun favoritesScreen_shows_handout_charts_elected() {
        val favorites = listOf(
            createCourse(id = 1, title = "Favorite 1"),
            createCourse(id = 2, title = "Favorite 2")
        )
        val viewModel = createViewModel(favorites = favorites)

        rule.setContent {
            ThousandsCoursesTheme {
                FavoritesScreen(viewModel = viewModel)
            }
        }

        rule.onNodeWithText("Favorite 1").assertIsDisplayed()
        rule.onNodeWithText("Favorite 2").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_number_cards_missing_with_number_selected_ones() {
        val favorites = listOf(
            createCourse(id = 1, title = "Course 1"),
            createCourse(id = 2, title = "Course 2"),
            createCourse(id = 3, title = "Course 3")
        )
        val viewModel = createViewModel(favorites = favorites)

        rule.setContent {
            ThousandsCoursesTheme {
                FavoritesScreen(viewModel = viewModel)
            }
        }

        rule.onAllNodes(hasClickAction()).assertCountEquals(3)
    }

    private fun createViewModel(favorites: List<Course>): FavoritesViewModel {
        val fakeRepo = FakeCourseRepository(favorites = favorites)
        return FavoritesViewModel(
            GetFavoritesCoursesUseCase(fakeRepo),
            ToggleCourseFavoriteStatusUseCase(fakeRepo)
        )
    }

    private fun createCourse(
        id: Int = 1,
        title: String = "Title"
    ) = Course(
        id = id,
        title = title,
        text = "Text",
        price = 1000,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = false,
        publishDate = LocalDate.now()
    )

    private class FakeCourseRepository(
        private val favorites: List<Course> = emptyList()
    ) : CourseRepository {
        override suspend fun getCourses() = Result.success(emptyList<Course>())
        override suspend fun getFavoritesCourses() = Result.success(flowOf(favorites))
        override suspend fun toggleCourseFavoriteStatus(course: Course) = Result.success(Unit)
        override suspend fun getCourseFromFavorites(courseId: Int): Result<Course?> =
            Result.success(favorites.find { it.id == courseId })
    }
}
