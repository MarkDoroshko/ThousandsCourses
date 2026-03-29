package com.example.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.validation.specific.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val emailValidator: EmailValidator
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun processIntent(intent: LoginIntent) {
        when (intent) {
            LoginIntent.DismissError -> {
                _state.update { it.copy(error = null) }
            }

            is LoginIntent.InputEmail -> {
                val emailError = if (intent.value.isBlank()) {
                    null
                } else if (!emailValidator.isValid(intent.value)) {
                    emailValidator.getDescription()
                } else {
                    null
                }

                _state.update { it.copy(email = intent.value, emailError = emailError) }
            }

            is LoginIntent.InputPassword -> {
                _state.update { it.copy(password = intent.value) }
            }

            LoginIntent.Submit -> {  // TODO: Реализовать нормальную авторизацию и навигацию при успехе
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateToMain)
                }
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

sealed interface LoginEffect {
    data object NavigateToMain : LoginEffect
}
