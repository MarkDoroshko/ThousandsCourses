package com.example.presentation.screen.login

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.validation.specific.EmailValidator
import com.example.presentation.theme.ThousandsCoursesTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(EmailValidator())
    }

    @Test
    fun loginScreen_entry_button_disabled_on_empty_fields() {
        rule.setContent {
            ThousandsCoursesTheme {
                LoginScreen(viewModel = viewModel, onSubmit = {})
            }
        }

        rule.onNode(hasText("Вход") and hasClickAction()).assertIsNotEnabled()
    }

    @Test
    fun loginScreen_input_button_active_recording_data() {
        rule.setContent {
            ThousandsCoursesTheme {
                LoginScreen(viewModel = viewModel, onSubmit = {})
            }
        }

        rule.onAllNodes(hasSetTextAction())[0].performTextInput("user@example.com")
        rule.onAllNodes(hasSetTextAction())[1].performTextInput("password123")

        rule.onNode(hasText("Вход") and hasClickAction()).assertIsEnabled()
    }

    @Test
    fun loginScreen_shows_message_with_error_in_wrong_email() {
        rule.setContent {
            ThousandsCoursesTheme {
                LoginScreen(viewModel = viewModel, onSubmit = {})
            }
        }

        rule.onAllNodes(hasSetTextAction())[0].performTextInput("invalid")

        rule.onNodeWithText("Ожидаемый формат: example@example.ru").assertIsDisplayed()
    }

    @Test
    fun loginScreen_does_not_show_error_when_empty_email() {
        rule.setContent {
            ThousandsCoursesTheme {
                LoginScreen(viewModel = viewModel, onSubmit = {})
            }
        }

        rule.onNodeWithText("Ожидаемый формат: example@example.ru").assertDoesNotExist()
    }
}
