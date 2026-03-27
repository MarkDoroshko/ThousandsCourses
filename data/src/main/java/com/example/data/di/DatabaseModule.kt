package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.CoursesDao
import com.example.data.local.database.CoursesDatabase
import com.example.data.local.model.CourseDbModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CoursesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = CoursesDatabase::class.java,
            name = "app_database.db"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    @Singleton
    fun provideCoursesDao(
        database: CoursesDatabase
    ): CoursesDao {
        return database.coursesDao()
    }
}