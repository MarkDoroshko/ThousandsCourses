package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppErrorSnackbarTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appErrorSnackbar_appear_with_errorMessage_not_null() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppErrorSnackbar(errorMessage = "Ошибка сети", onDismiss = {})
            }
        }

        rule.onNodeWithText("Ошибка сети").assertIsDisplayed()
    }

    @Test
    fun appErrorSnackbar_not_appear_with_null_errorMessage() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppErrorSnackbar(errorMessage = null, onDismiss = {})
            }
        }

        rule.onNodeWithText("Ошибка сети").assertDoesNotExist()
    }

    @Test
    fun appErrorSnackbar_click_on_dismiss_call_onDismiss() {
        var dismissed = false

        rule.setContent {
            ThousandsCoursesTheme {
                AppErrorSnackbar(errorMessage = "Ошибка", onDismiss = { dismissed = true })
            }
        }

        rule.onNode(hasClickAction()).performClick()

        assertTrue(dismissed)
    }

    @Test
    fun appErrorSnackbar_dismiss_after_four_seconds() {
        var dismissed = false
        rule.mainClock.autoAdvance = false

        rule.setContent {
            ThousandsCoursesTheme {
                AppErrorSnackbar(errorMessage = "Ошибка", onDismiss = { dismissed = true })
            }
        }

        rule.mainClock.advanceTimeBy(4001)

        assertTrue(dismissed)
    }
}
