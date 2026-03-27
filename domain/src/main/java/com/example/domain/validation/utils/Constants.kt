package com.example.domain.validation.utils

object Constants {
    val REGEX_EMAIL =
        """^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$""".toRegex()
}
