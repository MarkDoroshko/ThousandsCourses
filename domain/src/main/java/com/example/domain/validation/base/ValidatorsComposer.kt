package com.example.domain.validation.base

class ValidatorsComposer<T>(
    private vararg val validators: Validator<T>
) : Validator<T> {
    private var lastError: String = ""

    override fun isValid(value: T): Boolean {
        lastError = ""
        validators.forEach { validator ->
            if (!validator.isValid(value)) {
                lastError = validator.getDescription()
                return false
            }
        }
        return true
    }

    override fun getDescription(): String = lastError
}