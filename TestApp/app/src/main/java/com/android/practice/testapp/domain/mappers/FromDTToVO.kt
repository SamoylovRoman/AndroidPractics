package com.android.practice.testapp.domain.mappers

import com.android.practice.testapp.data.objects.CourseDT
import com.android.practice.testapp.presentation.view_objects.CourseVO

fun CourseDT.toVO() = CourseVO(
    id = id,
    title = title
)