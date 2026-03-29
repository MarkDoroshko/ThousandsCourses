package com.example.presentation.component

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppTextFieldTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun appTextField_show_placeholder() {
        rule.setContent {
            ThousandsCoursesTheme {
                AppTextField(
                    value = "",
                    onValueChange = {},
                    placeholderText = "example@example.com",
                    height = 56.dp,
                    paddingStart = 16.dp,
                    color = Color.White,
                    radius = 28.dp
                )
            }
        }

        rule.onNodeWithText("example@example.com").assertIsDisplayed()
    }

    @Test
    fun appTextField_show_current_text() {
        var text by mutableStateOf("")

        rule.setContent {
            ThousandsCoursesTheme {
                AppTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholderText = "Введите текст",
                    height = 56.dp,
                    paddingStart = 16.dp,
                    color = Color.White,
                    radius = 28.dp
                )
            }
        }

        rule.onNode(hasSetTextAction()).performTextInput("hello")

        rule.onNodeWithText("hello").assertIsDisplayed()
    }

    @Test
    fun appTextField_call_onValueChange_when_entering() {
        var capturedValue = ""

        rule.setContent {
            ThousandsCoursesTheme {
                AppTextField(
                    value = "",
                    onValueChange = { capturedValue = it },
                    height = 56.dp,
                    paddingStart = 16.dp,
                    color = Color.White,
                    radius = 28.dp
                )
            }
        }

        rule.onNode(hasSetTextAction()).performTextInput("h")

        assertEquals(capturedValue, "h")
    }
}
