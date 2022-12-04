package com.android.practice.testapp.presentation.courses_list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.testapp.R
import com.android.practice.testapp.databinding.ItemCourseBinding
import com.android.practice.testapp.presentation.extensions.inflate
import com.android.practice.testapp.presentation.view_objects.CourseVO
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class CourseAdapterDelegate(
    private val onCourseClick: (CourseVO) -> Unit,
    private val onCourseLongClick: (CourseVO) -> Boolean
) : AbsListItemAdapterDelegate<CourseVO, CourseVO, CourseAdapterDelegate.Holder>() {

    override fun isForViewType(
        item: CourseVO,
        items: MutableList<CourseVO>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_course), onCourseClick, onCourseLongClick)
    }

    override fun onBindViewHolder(item: CourseVO, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        private val containerView: View,
        onCourseClick: (CourseVO) -> Unit,
        onCourseLongClick: (CourseVO) -> Boolean
    ) : RecyclerView.ViewHolder(containerView) {

        private var currentCourse: CourseVO? = null
        private val binding: ItemCourseBinding by viewBinding()

        init {
            containerView.setOnClickListener { currentCourse?.let(onCourseClick) }
            containerView.setOnLongClickListener { currentCourse?.let(onCourseLongClick) ?: false }
        }

        fun bind(course: CourseVO) {
            currentCourse = course
            binding.courseTitle.text = course.title
        }
    }
}