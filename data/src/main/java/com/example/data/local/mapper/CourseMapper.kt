package com.example.data.local.mapper

import com.example.data.local.model.CourseDbModel
import com.example.domain.entity.Course

fun Course.toDbModel(): CourseDbModel {
    return CourseDbModel(id, title, text, price, rate, startDate, hasLike, publishDate)
}

fun CourseDbModel.toEntity(): Course {
    return Course(id, title, text, price, rate, startDate, hasLike, publishDate)
}

fun List<CourseDbModel>.toEntities(): List<Course> {
    return this.map { it.toEntity() }
}