package com.android.practice.testapp.presentation.courses_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.practice.testapp.domain.usecases.CoursesUseCase
import com.android.practice.testapp.presentation.utils.SingleLiveEvent
import com.android.practice.testapp.presentation.view_objects.CourseVO
import kotlinx.coroutines.launch

class CoursesListViewModel(private val coursesUseCase: CoursesUseCase) : ViewModel() {

    private val _contactsInList = MutableLiveData<List<CourseVO>>()
    val contactsInList: LiveData<List<CourseVO>>
        get() = _contactsInList

    private val _error = SingleLiveEvent<String>()
    val error: LiveData<String>
        get() = _error

    fun getAllCourses() {
        viewModelScope.launch {
            _contactsInList.postValue(coursesUseCase.getAllCourses())
        }
    }

    fun addNewCourse(title: String) {
        viewModelScope.launch {
            val result = coursesUseCase.addNewCourse(title)
            if (result) {
                getAllCourses()
            } else {
                _error.postValue("Couldn't add new course")
            }
        }
    }

    fun deleteCourseById(id: Long) {
        viewModelScope.launch {
            val result = coursesUseCase.deleteCourse(id)
            if (result) {
                getAllCourses()
            } else {
                _error.postValue("Couldn't delete this course")
            }
        }
    }

    fun deleteAllCourses() {
        viewModelScope.launch {
            val deleteStrings = coursesUseCase.deleteAllCourses()
            if (deleteStrings > 0) {
                getAllCourses()
            } else {
                _error.postValue("Nothing to delete")
            }
        }
    }

    fun updateCourseById(id: Long, title: String) {
        viewModelScope.launch {
            val result = coursesUseCase.updateCourse(id, title)
            if (result) {
                getAllCourses()
            } else {
                _error.postValue("Couldn't update this course")
            }
        }
    }
}