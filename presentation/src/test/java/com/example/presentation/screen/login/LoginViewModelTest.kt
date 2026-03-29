package com.example.presentation.screen.login

import com.example.domain.validation.specific.EmailValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("LoginViewModel")
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        viewModel = LoginViewModel(EmailValidator())
    }

    @Test
    @DisplayName("При пустом email не устанавливает emailError")
    fun blankEmailDoesNotSetEmailError() {
        viewModel.processIntent(LoginIntent.InputEmail(""))

        assertNull(viewModel.state.value.emailError)
    }

    @Test
    @DisplayName("При невалидном email устанавливает emailError")
    fun invalidEmailSetsEmailError() {
        viewModel.processIntent(LoginIntent.InputEmail("invalid"))

        assertNotNull(viewModel.state.value.emailError)
    }

    @Test
    @DisplayName("При невалидном email устанавливает корректное сообщение об ошибке")
    fun invalidEmailSetsCorrectErrorMessage() {
        viewModel.processIntent(LoginIntent.InputEmail("invalid"))

        assertEquals(viewModel.state.value.emailError, "Ожидаемый формат: example@example.ru")
    }

    @Test
    @DisplayName("При валидном email не устанавливает emailError")
    fun validEmailDoesNotSetEmailError() {
        viewModel.processIntent(LoginIntent.InputEmail("user@example.com"))

        assertNull(viewModel.state.value.emailError)
    }

    @Test
    @DisplayName("InputEmail обновляет поле email")
    fun inputEmailUpdatesEmailField() {
        val email = "user@example.com"

        viewModel.processIntent(LoginIntent.InputEmail(email))

        assertEquals(viewModel.state.value.email, email)
    }

    @Test
    @DisplayName("InputPassword обновляет поле password")
    fun inputPasswordUpdatesPasswordField() {
        val password = "password123"

        viewModel.processIntent(LoginIntent.InputPassword(password))

        assertEquals(viewModel.state.value.password, password)
    }

    @Test
    @DisplayName("DismissError обнуляет error")
    fun dismissErrorClearsError() {
        viewModel.processIntent(LoginIntent.DismissError)

        assertNull(viewModel.state.value.error)
    }

    @Test
    @DisplayName("isSubmitButtonEnabled = true при непустых email, password и отсутствии ошибок")
    fun submitButtonEnabledWhenEmailAndPasswordSetAndNoErrors() {
        viewModel.processIntent(LoginIntent.InputEmail("user@example.com"))
        viewModel.processIntent(LoginIntent.InputPassword("password123"))

        assertTrue { viewModel.state.value.isSubmitButtonEnabled }
    }

    @Test
    @DisplayName("isSubmitButtonEnabled = false при наличии emailError")
    fun submitButtonDisabledWhenEmailErrorPresent() {
        viewModel.processIntent(LoginIntent.InputEmail("invalid"))
        viewModel.processIntent(LoginIntent.InputPassword("password123"))

        assertFalse { viewModel.state.value.isSubmitButtonEnabled }
    }

    @Test
    @DisplayName("isSubmitButtonEnabled = false при пустом password")
    fun submitButtonDisabledWhenPasswordIsBlank() {
        viewModel.processIntent(LoginIntent.InputEmail("user@example.com"))

        assertFalse { viewModel.state.value.isSubmitButtonEnabled }
    }

    @Test
    @DisplayName("isSubmitButtonEnabled = false при пустом email")
    fun submitButtonDisabledWhenEmailIsBlank() {
        viewModel.processIntent(LoginIntent.InputPassword("password123"))

        assertFalse { viewModel.state.value.isSubmitButtonEnabled }
    }
}
