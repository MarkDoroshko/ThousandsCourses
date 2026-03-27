package com.example.data.remote.service

import com.example.data.remote.dto.CoursesResponse
import retrofit2.http.GET

interface CourseApiService {
    @GET("https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download")
    suspend fun getCourses(): CoursesResponse
}