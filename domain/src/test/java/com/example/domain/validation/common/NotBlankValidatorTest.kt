package com.example.domain.validation.common

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@DisplayName("NotBlankValidator")
class NotBlankValidatorTest {
    private lateinit var notBlankValidator: NotBlankValidator

    @BeforeEach
    fun setUp() {
        notBlankValidator = NotBlankValidator()
    }

    @Test
    @DisplayName("Для непустой строки возвращает true")
    fun isValidValue() {
        assertTrue {
            notBlankValidator.isValid("test")
        }
    }

    @Test
    @DisplayName("Для пустой строки возвращает false")
    fun blankValue() {
        assertFalse {
            notBlankValidator.isValid("")
        }
    }

    @Test
    @DisplayName("Для строки из пробелов возвращает false")
    fun withSpacesValue() {
        assertFalse {
            notBlankValidator.isValid("   ")
        }
    }

    @Test
    @DisplayName("Возвращает корректное описание без переданного fieldName")
    fun getDescriptionWithoutFieldName() {
        assertEquals(notBlankValidator.getDescription(), "Поле не может быть пустым")
    }

    @Test
    @DisplayName("Возвращает корректное описание с переданным fieldName")
    fun getDescriptionWithFieldName() {
        val fieldName = "Some field"

        assertEquals(NotBlankValidator(fieldName).getDescription(), "Some field не может быть пустым")
    }
}