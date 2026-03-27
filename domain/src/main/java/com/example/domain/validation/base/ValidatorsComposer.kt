package com.example.domain.validation.base

class ValidatorsComposer<T>(
    private vararg val validators: Validator<T>
) : Validator<T> {
    private var description: String = ""
    private var failedValidator: Validator<T>? = null

    override fun isValid(value: T): Boolean {
        description = ""
        failedValidator = null

        validators.forEach { validator ->
            if (!validator.isValid(value)) {
                description = validator.getDescription()
                failedValidator = validator
                return false
            }
        }
        return true
    }

    override fun getDescription(): String {
        return if (failedValidator != null) {
            "${failedValidator?.getDescription()}"
        } else {
            description
        }
    }
}