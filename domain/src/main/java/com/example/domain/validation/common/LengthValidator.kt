package com.example.domain.validation.common

import com.example.domain.validation.base.Validator

class LengthValidator(
    private val fieldName: String = "Поле",
    private val min: Int,
    private val max: Int
) : Validator<String> {

    init {
        require(min <= max) { "min должно быть меньше или равно max" }
    }

    override fun isValid(value: String): Boolean =
        value.length in min..max

    override fun getDescription(): String =
        if (min == max) {
            "$fieldName должно содержать ровно $min символов"
        } else {
            "$fieldName должно содержать от $min до $max символов"
        }
}