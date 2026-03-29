package com.example.domain.validation.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@DisplayName("LengthValidator")
class LengthValidatorTest {
    @Test
    @DisplayName("При попадании в диапазон возвращает true")
    fun fallingIntoRange() {
        val lengthValidator = LengthValidator(min = 5, max = 10)

        assertTrue {
            lengthValidator.isValid("1234567")
            lengthValidator.isValid("123456")
            lengthValidator.isValid("12345678")
        }
    }

    @Test
    @DisplayName("При непопадании в диапазон возвращает false")
    fun notFallingIntoRange() {
        val lengthValidator = LengthValidator(min = 5, max = 10)

        assertFalse {
            lengthValidator.isValid("1234")
            lengthValidator.isValid("123")
            lengthValidator.isValid("12345678912")
        }
    }

    @Test
    @DisplayName("При нахождение на границе диапазона возвращает true")
    fun edgeOfRange() {
        val lengthValidator = LengthValidator(min = 5, max = 10)

        assertTrue {
            lengthValidator.isValid("12345")
            lengthValidator.isValid("1234567891")
        }
    }

    @Test
    @DisplayName("Выкидывает ошибку при min > max")
    fun errorWhenMinMoreMax() {
        assertThrows<IllegalArgumentException> {
            LengthValidator(min = 10, max = 5)
        }
    }

    @Test
    @DisplayName("Возвращает валидное описание без fieldName")
    fun validDescriptionWithoutFieldName() {
        var lengthValidator = LengthValidator(min = 5, max = 10)

        assertEquals(lengthValidator.getDescription(), "Поле должно содержать от 5 до 10 символов")

        lengthValidator = LengthValidator(min = 5, max = 5)

        assertEquals(lengthValidator.getDescription(), "Поле должно содержать ровно 5 символов")
    }

    @Test
    @DisplayName("Возвращает валидное описание с fieldName")
    fun validDescriptionWithFieldName() {
        var lengthValidator = LengthValidator(fieldName = "Some field", min = 5, max = 10)

        assertEquals(lengthValidator.getDescription(), "Some field должно содержать от 5 до 10 символов")

        lengthValidator = LengthValidator(fieldName = "Some field", min = 5, max = 5)

        assertEquals(lengthValidator.getDescription(), "Some field должно содержать ровно 5 символов")
    }
}