package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppButtonTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appButton_show_text() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppButton(text = "Вход", onClick = {}, enabled = true, isLoading = false)
            }
        }

        rule.onNodeWithText("Вход").assertIsDisplayed()
    }

    @Test
    fun appButton_disabled_when_enabled_false() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppButton(text = "Вход", onClick = {}, enabled = false, isLoading = false)
            }
        }

        rule.onNodeWithText("Вход").assertIsNotEnabled()
    }

    @Test
    fun appButton_activated_when_enabled_true() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppButton(text = "Вход", onClick = {}, enabled = true, isLoading = false)
            }
        }

        rule.onNodeWithText("Вход").assertIsEnabled()
    }

    @Test
    fun appButton_show_loading_indicator_when_isLoading_true() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppButton(text = "Вход", onClick = {}, enabled = true, isLoading = true)
            }
        }

        rule.onNodeWithText("Вход").assertDoesNotExist()
        rule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }
}
