package com.example.domain.validation.base

interface Validator<T> {

    fun isValid(value: T): Boolean

    fun getDescription(): String
}