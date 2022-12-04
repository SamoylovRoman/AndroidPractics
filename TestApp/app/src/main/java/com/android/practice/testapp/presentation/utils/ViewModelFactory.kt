package com.android.practice.testapp.presentation.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.practice.testapp.data.repository.CourseRepositoryImpl
import com.android.practice.testapp.domain.usecases.CoursesUseCaseImpl
import com.android.practice.testapp.presentation.courses_list.CoursesListViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val coursesRepository by lazy(LazyThreadSafetyMode.NONE) {
        CourseRepositoryImpl(context = context)
    }

    private val coursesUseCase by lazy(LazyThreadSafetyMode.NONE) {
        CoursesUseCaseImpl(repository = coursesRepository)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        CoursesListViewModel::class.java -> CoursesListViewModel(
            coursesUseCase = coursesUseCase
        )
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T
}