package com.example.domain.validation.common

import com.example.domain.validation.base.Validator

class RegexValidator(
    private val pattern: Regex,
    private val errorMessage: String
) : Validator<String> {

    override fun isValid(value: String): Boolean = pattern.matches(value)

    override fun getDescription(): String = errorMessage
}