package com.example.domain.entity

import java.time.LocalDate

data class Course(
    val id: Int,
    val title: String,
    val text: String,
    val price: Int,
    val rate: Double,
    val startDate: LocalDate,
    val hasLike: Boolean,
    val publishDate: LocalDate
)
