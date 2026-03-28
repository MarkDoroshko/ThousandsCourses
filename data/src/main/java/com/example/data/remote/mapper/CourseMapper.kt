package com.example.data.remote.mapper

import com.example.data.remote.dto.CourseDto
import com.example.data.remote.dto.CoursesResponse
import com.example.domain.entity.Course
import java.time.LocalDate

fun CourseDto.toEntity(): Course {
    return Course(
        id = id,
        title = title,
        text = text,
        price = price.replace(" ", "").toInt(),
        rate = rate.toDouble(),
        startDate = LocalDate.parse(startDate),
        hasLike = hasLike,
        publishDate = LocalDate.parse(publishDate)
    )
}

fun CoursesResponse.toListCourse(): List<Course> {
    return courses.map { it.toEntity() }
}