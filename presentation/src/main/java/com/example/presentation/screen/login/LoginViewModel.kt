package com.example.presentation.screen.login

import androidx.lifecycle.ViewModel
import com.example.domain.validation.specific.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val emailValidator: EmailValidator
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.DismissError -> {

            }

            is LoginIntent.InputEmail -> {

            }

            is LoginIntent.InputPassword -> {

            }

            LoginIntent.Submit -> {

            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null
) {
    val isSubmitButtonEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && emailError == null && !isLoading
}

sealed interface LoginIntent {
    data class InputEmail(
        val value: String
    ) : LoginIntent

    data class InputPassword(
        val value: String
    ) : LoginIntent

    data object Submit : LoginIntent

    data object DismissError : LoginIntent
}