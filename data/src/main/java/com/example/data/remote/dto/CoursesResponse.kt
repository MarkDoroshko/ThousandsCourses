package com.example.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoursesResponse(
    @SerialName("courses")
    val courses: List<CourseDto>
)
