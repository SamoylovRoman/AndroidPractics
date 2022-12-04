package com.android.practice.testapp.domain.repository

import com.android.practice.testapp.data.objects.CourseDT

interface CoursesRepository {

    suspend fun getAllCourses(): List<CourseDT>

    suspend fun getCourseDetails(id: Long): CourseDT

    suspend fun addNewCourse(title: String): Boolean

    suspend fun deleteCourse(id: Long): Boolean

    suspend fun updateCourse(id: Long, title: String): Boolean

    suspend fun deleteAllCourses(): Int
}