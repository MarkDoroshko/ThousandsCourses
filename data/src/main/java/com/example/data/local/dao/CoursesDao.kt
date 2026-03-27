package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.model.CourseDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCourse(courseDbModel: CourseDbModel): Long

    @Delete
    suspend fun deleteCourse(courseDbModel: CourseDbModel)

    @Query("SELECT * FROM courses")
    fun getCourses(): Flow<List<CourseDbModel>>
}