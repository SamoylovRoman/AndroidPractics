package com.android.practice.testapp.domain.usecases

import com.android.practice.testapp.presentation.view_objects.CourseVO

interface CoursesUseCase {

    suspend fun getAllCourses(): List<CourseVO>

    suspend fun getCourseDetails(id: Long): CourseVO

    suspend fun addNewCourse(title: String): Boolean

    suspend fun deleteCourse(id: Long): Boolean

    suspend fun updateCourse(id: Long, title: String): Boolean

    suspend fun deleteAllCourses(): Int
}