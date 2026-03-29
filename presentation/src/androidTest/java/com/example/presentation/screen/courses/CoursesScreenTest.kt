@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.presentation.screen.courses

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.entity.Course
import com.example.domain.repository.CourseRepository
import com.example.domain.usecase.GetCoursesUseCase
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
class CoursesScreenTest {
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
    fun coursesScreen_lazyColumn_show_all_items() {
        val courses = listOf(
            createCourse(id = 1, title = "Course 1"),
            createCourse(id = 2, title = "Course 2"),
            createCourse(id = 3, title = "Course 3")
        )
        val viewModel = createViewModel(courses = courses)

        rule.setContent {
            ThousandsCoursesTheme {
                CoursesScreen(viewModel = viewModel)
            }
        }

        rule.onNodeWithText("Course 1").assertIsDisplayed()
        rule.onNodeWithText("Course 2").assertIsDisplayed()
        rule.onNodeWithText("Course 3").assertIsDisplayed()
    }

    @Test
    fun coursesScreen_search_interactive_field() {
        val viewModel = createViewModel()

        rule.setContent {
            ThousandsCoursesTheme {
                CoursesScreen(viewModel = viewModel)
            }
        }

        rule.onAllNodes(hasSetTextAction())[0].assertIsDisplayed()
    }

    @Test
    fun coursesScreen_button_sorting_clickable() {
        val viewModel = createViewModel()

        rule.setContent {
            ThousandsCoursesTheme {
                CoursesScreen(viewModel = viewModel)
            }
        }

        rule.onAllNodes(hasClickAction())[0].assertIsEnabled()
    }

    private fun createViewModel(
        courses: List<Course> = emptyList(),
        favorites: List<Course> = emptyList()
    ): CoursesViewModel {
        val fakeRepo = FakeCourseRepository(courses = courses, favorites = favorites)
        return CoursesViewModel(
            GetCoursesUseCase(fakeRepo),
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
        private val courses: List<Course> = emptyList(),
        private val favorites: List<Course> = emptyList()
    ) : CourseRepository {
        override suspend fun getCourses() = Result.success(courses)
        override suspend fun getFavoritesCourses() = Result.success(flowOf(favorites))
        override suspend fun toggleCourseFavoriteStatus(course: Course) = Result.success(Unit)
        override suspend fun getCourseFromFavorites(courseId: Int): Result<Course?> =
            Result.success(favorites.find { it.id == courseId })
    }
}
