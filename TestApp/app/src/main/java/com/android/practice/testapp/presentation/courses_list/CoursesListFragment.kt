package com.android.practice.testapp.presentation.courses_list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.testapp.R
import com.android.practice.testapp.databinding.FragmentCoursesListBinding
import com.android.practice.testapp.databinding.FragmentDialogBinding
import com.android.practice.testapp.presentation.courses_list.adapter.CourseListAdapter
import com.android.practice.testapp.presentation.utils.ViewModelFactory
import com.android.practice.testapp.presentation.utils.autoCleared
import com.android.practice.testapp.presentation.view_objects.CourseVO

class CoursesListFragment : Fragment(R.layout.fragment_courses_list) {

    private val binding: FragmentCoursesListBinding by viewBinding()
    private var courseAdapter: CourseListAdapter by autoCleared()

    //    private val fragmentDialog: FragmentDialogBinding by viewBinding()
    private var _dialogBinding: FragmentDialogBinding? = null
    private val dialogBinding: FragmentDialogBinding get() = _dialogBinding!!

    private val viewModel: CoursesListViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCoursesList()
        initListeners()
        bindViewModel()
    }

    private fun initListeners() {
        binding.addCourseButton.setOnClickListener {
            showDialog()
        }
        binding.deleteAllCoursesButton.setOnClickListener {
            showDialog()
        }
    }

    private fun bindViewModel() {
        viewModel.error.observe(viewLifecycleOwner) { message ->
            showError(message)
        }
        viewModel.contactsInList.observe(viewLifecycleOwner) { newList ->
            courseAdapter.items = newList
        }
    }

    private fun initCoursesList() {
        courseAdapter = CourseListAdapter({ course ->
            showDialog(course)
        }) { course ->
            showDeletingDialog(course)
        }
    }

    private fun showDeletingDialog(course: CourseVO): Boolean {
        AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_report)
            .setMessage(getString(R.string.to_remove_course))
            .setPositiveButton("Yes, I do") { dialog, _ ->
                viewModel.deleteCourseById(course.id)
                dialog.cancel()
            }
            .setNegativeButton("No, I don't") { dialog, _ ->
                dialog.cancel()
            }
            .show()
        return true
    }

    private fun showDialog(course: CourseVO? = null) {
        val alertDialog = AlertDialog.Builder(requireContext())
        _dialogBinding = FragmentDialogBinding.inflate(LayoutInflater.from(requireContext()))
//        alertDialog.setView(fragmentDialog.root)
        dialogBinding.titleEditText.setText("Hello! How are you?")
        alertDialog.setView(dialogBinding.root)
            .setTitle(
                if (course == null) "Add new course"
                else "Course details"
            )
            .setMessage(getString(R.string.course_title))
            .setPositiveButton("OK") { _, _ ->

                when (course) {
                    null -> viewModel.addNewCourse(dialogBinding.titleEditText.text.toString())
                    else -> viewModel.updateCourseById(
                        course.id,
                        dialogBinding.titleEditText.toString()
                    )
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
//        fragmentDialog.titleEditText.addTextChangedListener(object : TextWatcher {
        dialogBinding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
//                courseTitle = dialogBinding.titleEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        alertDialog.show()
    }

    private fun showError(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error)
            .setIcon(R.drawable.ic_error)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}