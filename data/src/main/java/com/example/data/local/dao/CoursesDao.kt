package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.model.CourseDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCourse(courseDbModel: CourseDbModel): Long

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourse(id: Int)

    @Query("SELECT * FROM courses")
    fun getCourses(): Flow<List<CourseDbModel>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourse(id: Int): CourseDbModel?
}