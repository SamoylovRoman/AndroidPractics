package com.android.practice.testapp.data.repository

import android.content.ContentProviderClient
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.android.practice.testapp.data.objects.CourseDT
import com.android.practice.testapp.domain.repository.CoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CourseRepositoryImpl(private val context: Context) : CoursesRepository {

//    private var newContentResolver: ContentProviderClient = context.contentResolver.acquireContentProviderClient(AUTHORITY)
//        ?: error("null")


    override suspend fun getAllCourses(): List<CourseDT> = withContext(Dispatchers.IO) {
        context.contentResolver.query(
            CONTENT_URI_COURSES,
            null,
            null,
            null,
            null
        )?.use {
            getCourseFromCursor(it)
        }.orEmpty()
    }

    private fun getCourseFromCursor(cursor: Cursor): List<CourseDT> {
        if (cursor.moveToFirst().not()) return emptyList()
        val coursesList = mutableListOf<CourseDT>()
        do {
            val id = cursor.getColumnIndex(COLUMN_COURSE_ID)
                .run(cursor::getLong)
            val title = cursor.getColumnIndex(COLUMN_COURSE_TITLE)
                .run(cursor::getString)
            coursesList.add(CourseDT(id = id, title = title))
        } while (cursor.moveToNext())
        return coursesList
    }

    override suspend fun getCourseDetails(id: Long): CourseDT {
        TODO("Not yet implemented")
    }

    override suspend fun addNewCourse(title: String) = withContext(Dispatchers.IO) {
        val id = Random.nextLong(0, Long.MAX_VALUE)
        val value = ContentValues().apply {
            put(COLUMN_COURSE_ID, id)
            put(COLUMN_COURSE_TITLE, title)
        }

//        val result = newContentResolver.insert(CONTENT_URI_COURSES, value)
        val result = context.contentResolver.insert(CONTENT_URI_COURSES, value)
        result != null
    }

    override suspend fun deleteCourse(id: Long): Boolean = withContext(Dispatchers.IO) {
        val courseUri = Uri.withAppendedPath(CONTENT_URI_COURSES, id.toString())
        return@withContext context.contentResolver.delete(courseUri, null, null) == 1
    }

    override suspend fun updateCourse(id: Long, title: String): Boolean =
        withContext(Dispatchers.IO) {
            val courseUri = Uri.withAppendedPath(CONTENT_URI_COURSES, id.toString())
            val values = ContentValues().apply {
                put(COLUMN_COURSE_TITLE, title)
            }
            context.contentResolver.update(courseUri, values, null, null) == 1
        }

    override suspend fun deleteAllCourses(): Int = withContext(Dispatchers.IO) {
        context.contentResolver.delete(CONTENT_URI_COURSES, null, null)
    }

    companion object {

//        private const val AUTHORITY = "com.android.practice.contentprovidernew.provider"

        private const val AUTHORITY =
            "com.android.practice.contentprovidernew.provider"
        private const val PATH_COURSES = "courses"
        private val CONTENT_URI_COURSES = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(PATH_COURSES)
            .build()

        //        private val CONTENT_URI_COURSES =Uri.parse()
        private const val COLUMN_COURSE_ID = "course_id"
        private const val COLUMN_COURSE_TITLE = "course_title"
    }

}
