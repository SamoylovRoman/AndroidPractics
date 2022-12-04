package com.android.practice.testapp.presentation.courses_list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.android.practice.testapp.presentation.view_objects.CourseVO
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CourseListAdapter(
    onCourseClick: (CourseVO) -> Unit,
    onCourseLongClick: (CourseVO) -> Boolean
) : AsyncListDifferDelegationAdapter<CourseVO>(ContactDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(CourseAdapterDelegate(onCourseClick, onCourseLongClick))
    }

    class ContactDiffUtilCallback : DiffUtil.ItemCallback<CourseVO>() {
        override fun areItemsTheSame(oldItem: CourseVO, newItem: CourseVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CourseVO, newItem: CourseVO): Boolean {
            return oldItem == newItem
        }
    }
}