package com.example.domain.validation.specific

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("EmailValidator")
class EmailValidatorTest {
    private lateinit var emailValidator: EmailValidator

    @BeforeEach
    fun setUp() {
        emailValidator = EmailValidator()
    }

    @Test
    @DisplayName("Для корректного email возвращает true")
    fun validEmail() {
        assertTrue {
            emailValidator.isValid("user@example.com")
            emailValidator.isValid("test.name+tag@mail.ru")
        }
    }

    @Test
    @DisplayName("Для пустой строки возвращает false")
    fun emptyEmail() {
        assertFalse {
            emailValidator.isValid("")
        }
    }

    @Test
    @DisplayName("Для строки из пробелов возвращает false")
    fun blankEmail() {
        assertFalse {
            emailValidator.isValid("   ")
        }
    }

    @Test
    @DisplayName("Для строки длиннее 100 символов возвращает false")
    fun tooLongEmail() {
        val longEmail = "a".repeat(90) + "@example.com"

        assertFalse {
            emailValidator.isValid(longEmail)
        }
    }

    @Test
    @DisplayName("Для email без символа @ возвращает false")
    fun emailWithoutAt() {
        assertFalse {
            emailValidator.isValid("userexample.com")
        }
    }

    @Test
    @DisplayName("Для email без домена возвращает false")
    fun emailWithoutDomain() {
        assertFalse {
            emailValidator.isValid("user@")
        }
    }

    @Test
    @DisplayName("Для email без расширения домена возвращает false")
    fun emailWithoutDomainExtension() {
        assertFalse {
            emailValidator.isValid("user@domain")
        }
    }

    @Test
    @DisplayName("После пустой строки getDescription возвращает ошибку NotBlankValidator")
    fun getDescriptionAfterBlank() {
        emailValidator.isValid("")

        assertEquals(emailValidator.getDescription(), "Поле не может быть пустым")
    }

    @Test
    @DisplayName("После строки длиннее 100 символов getDescription возвращает ошибку LengthValidator")
    fun getDescriptionAfterTooLong() {
        val longEmail = "a".repeat(90) + "@example.com"
        emailValidator.isValid(longEmail)

        assertEquals(emailValidator.getDescription(), "Поле должно содержать от 1 до 100 символов")
    }

    @Test
    @DisplayName("После некорректного формата getDescription возвращает ошибку RegexValidator")
    fun getDescriptionAfterInvalidFormat() {
        emailValidator.isValid("userexample.com")

        assertEquals(emailValidator.getDescription(), "Ожидаемый формат: example@example.ru")
    }
}
