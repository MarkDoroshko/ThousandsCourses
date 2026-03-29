package com.example.domain.validation.base

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("ValidatorsComposer")
class ValidatorsComposerTest {

    @Test
    @DisplayName("Когда все валидаторы проходят, возвращает true")
    fun allValidatorsPass() {
        val validator1 = mock<Validator<String>>()
        val validator2 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(true)
        whenever(validator2.isValid(any())).thenReturn(true)
        val composer = ValidatorsComposer(validator1, validator2)

        assertTrue { composer.isValid("test") }
    }

    @Test
    @DisplayName("Когда первый валидатор не проходит, возвращает false")
    fun firstValidatorFails() {
        val validator1 = mock<Validator<String>>()
        val validator2 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(false)
        val composer = ValidatorsComposer(validator1, validator2)

        assertFalse { composer.isValid("test") }
    }

    @Test
    @DisplayName("Когда первый валидатор не проходит, второй не вызывается")
    fun secondValidatorNotCalledWhenFirstFails() {
        val validator1 = mock<Validator<String>>()
        val validator2 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(false)
        val composer = ValidatorsComposer(validator1, validator2)

        composer.isValid("test")

        verify(validator2, never()).isValid(any())
    }

    @Test
    @DisplayName("Когда средний валидатор не проходит, возвращает false")
    fun middleValidatorFails() {
        val validator1 = mock<Validator<String>>()
        val validator2 = mock<Validator<String>>()
        val validator3 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(true)
        whenever(validator2.isValid(any())).thenReturn(false)
        val composer = ValidatorsComposer(validator1, validator2, validator3)

        assertFalse { composer.isValid("test") }
    }

    @Test
    @DisplayName("После успешной валидации getDescription возвращает пустую строку")
    fun getDescriptionAfterSuccess() {
        val validator1 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(true)
        val composer = ValidatorsComposer(validator1)

        composer.isValid("test")

        assertEquals(composer.getDescription(), "")
    }

    @Test
    @DisplayName("После неуспешной валидации getDescription возвращает описание первого упавшего валидатора")
    fun getDescriptionAfterFailure() {
        val validator1 = mock<Validator<String>>()
        val validator2 = mock<Validator<String>>()
        whenever(validator1.isValid(any())).thenReturn(true)
        whenever(validator2.isValid(any())).thenReturn(false)
        whenever(validator2.getDescription()).thenReturn("Ошибка валидатора 2")
        val composer = ValidatorsComposer(validator1, validator2)

        composer.isValid("test")

        assertEquals(composer.getDescription(), "Ошибка валидатора 2")
    }
}
