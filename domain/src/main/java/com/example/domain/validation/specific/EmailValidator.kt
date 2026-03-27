package com.example.domain.validation.specific

import com.example.domain.validation.base.Validator
import com.example.domain.validation.base.ValidatorsComposer
import com.example.domain.validation.common.LengthValidator
import com.example.domain.validation.common.NotBlankValidator
import com.example.domain.validation.common.RegexValidator
import com.example.domain.validation.utils.Constants
import javax.inject.Inject

class EmailValidator @Inject constructor() : Validator<String> {

    private val composer = ValidatorsComposer(
        NotBlankValidator(),
        LengthValidator(min = 1, max = 100),
        RegexValidator(
            pattern = Constants.REGEX_EMAIL,
            errorMessage = "Ожидаемый формат: example@example.ru"
        )
    )

    override fun isValid(value: String): Boolean = composer.isValid(value)

    override fun getDescription(): String = composer.getDescription()
}