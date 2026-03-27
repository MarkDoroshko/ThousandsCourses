package com.example.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.CoursesDao
import com.example.data.local.model.CourseDbModel

@Database(
    entities = [CourseDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class CoursesDatabase : RoomDatabase() {
    abstract fun coursesDao(): CoursesDao
}