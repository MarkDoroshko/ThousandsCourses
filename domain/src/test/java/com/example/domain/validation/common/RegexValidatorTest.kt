package com.example.domain.validation.common

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("RegexValidator")
class RegexValidatorTest {

    @Test
    @DisplayName("Для строки, совпадающей с паттерном, возвращает true")
    fun matchesPattern() {
        val regexValidator = RegexValidator(
            pattern = Regex("[a-z]+"),
            errorMessage = "Ошибка"
        )

        assertTrue {
            regexValidator.isValid("abc")
            regexValidator.isValid("hello")
        }
    }

    @Test
    @DisplayName("Для строки, не совпадающей с паттерном, возвращает false")
    fun notMatchesPattern() {
        val regexValidator = RegexValidator(
            pattern = Regex("[a-z]+"),
            errorMessage = "Ошибка"
        )

        assertFalse {
            regexValidator.isValid("ABC")
            regexValidator.isValid("123")
            regexValidator.isValid("abc123")
        }
    }

    @Test
    @DisplayName("Для пустой строки при паттерне, не допускающем пустую строку, возвращает false")
    fun emptyStringNotMatchesPattern() {
        val regexValidator = RegexValidator(
            pattern = Regex("[a-z]+"),
            errorMessage = "Ошибка"
        )

        assertFalse {
            regexValidator.isValid("")
        }
    }

    @Test
    @DisplayName("Возвращает переданное сообщение об ошибке как описание")
    fun getDescription() {
        val errorMessage = "Некорректный формат"
        val regexValidator = RegexValidator(
            pattern = Regex("[a-z]+"),
            errorMessage = errorMessage
        )

        assertEquals(regexValidator.getDescription(), errorMessage)
    }
}
