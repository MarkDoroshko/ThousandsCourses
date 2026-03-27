package com.example.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "courses")
data class CourseDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val text: String,
    val price: Int,
    val rate: Double,
    val startDate: LocalDate,
    val hasLike: Boolean,
    val publishDate: LocalDate
)
