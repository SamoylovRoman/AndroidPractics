package com.android.practice.testapp.domain.usecases

import com.android.practice.testapp.data.repository.CourseRepositoryImpl
import com.android.practice.testapp.domain.mappers.toVO
import com.android.practice.testapp.presentation.view_objects.CourseVO

class CoursesUseCaseImpl(private val repository: CourseRepositoryImpl) : CoursesUseCase {

    override suspend fun getAllCourses(): List<CourseVO> {
        return repository.getAllCourses().map { it.toVO() }
    }

    override suspend fun getCourseDetails(id: Long): CourseVO {
        return repository.getCourseDetails(id).toVO()
    }

    override suspend fun addNewCourse(title: String): Boolean {
        return repository.addNewCourse(title)
    }

    override suspend fun deleteCourse(id: Long): Boolean {
        return repository.deleteCourse(id)
    }

    override suspend fun updateCourse(id: Long, title: String): Boolean {
        return repository.updateCourse(id, title)
    }

    override suspend fun deleteAllCourses(): Int {
        return repository.deleteAllCourses()
    }
}