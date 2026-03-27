package com.example.domain.validation.common

import com.example.domain.validation.base.Validator

class NotBlankValidator(
    private val fieldName: String = "Поле"
) : Validator<String> {

    override fun isValid(value: String): Boolean = value.isNotBlank()

    override fun getDescription(): String = "$fieldName не может быть пустым"
}