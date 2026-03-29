package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.entity.Course
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class CourseItemTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun courseItem_show_title_course() {
        rule.setContent {
            ThousandsCoursesTheme {
                CourseItem(course = createCourse(title = "Kotlin Basic"), isFavorite = false, onToggleFavorite = {})
            }
        }

        rule.onNodeWithText("Kotlin Basic").assertIsDisplayed()
    }

    @Test
    fun courseItem_show_price() {
        rule.setContent {
            ThousandsCoursesTheme {
                CourseItem(course = createCourse(price = 2500), isFavorite = false, onToggleFavorite = {})
            }
        }

        rule.onNodeWithText("2500 ₽").assertIsDisplayed()
    }

    @Test
    fun courseItem_show_when_isFavorite_true() {
        rule.setContent {
            ThousandsCoursesTheme {
                CourseItem(course = createCourse(), isFavorite = true, onToggleFavorite = {})
            }
        }

        rule.onNodeWithText(createCourse().title).assertIsDisplayed()
    }

    @Test
    fun courseItem_show_when_isFavorite_false() {
        rule.setContent {
            ThousandsCoursesTheme {
                CourseItem(course = createCourse(), isFavorite = false, onToggleFavorite = {})
            }
        }

        rule.onNodeWithText(createCourse().title).assertIsDisplayed()
    }

    @Test
    fun courseItem_click_on_favorite_icon_call_callback() {
        var toggled = false

        rule.setContent {
            ThousandsCoursesTheme {
                CourseItem(course = createCourse(), isFavorite = false, onToggleFavorite = { toggled = true })
            }
        }

        rule.onNode(hasClickAction()).performClick()

        assertTrue(toggled)
    }

    private fun createCourse(
        title: String = "Title",
        price: Int = 1000
    ) = Course(
        id = 1,
        title = title,
        text = "Text",
        price = price,
        rate = 4.5,
        startDate = LocalDate.now(),
        hasLike = false,
        publishDate = LocalDate.now()
    )
}
