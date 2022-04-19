package com.example.viewpager

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FilterDialogFragment : DialogFragment() {

    private lateinit var tags: BooleanArray

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        tags = arguments?.getSerializable(TAGS_TAG) as BooleanArray
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_dialog_caption))
            .setPositiveButton(getString(R.string.text_positive_button)) { _, _ ->
                (parentFragment as FilterDialogCallBack).onOKButtonClick(tags)
            }
            .setMultiChoiceItems(
                ArticleTag.values().map { tag -> tag.toString() }.toTypedArray(),
                tags
            ) { _, which, isChecked ->
                tags[which] = isChecked
            }
            .create()
    }

    companion object {

        fun newInstance(argTags: BooleanArray): FilterDialogFragment {
            val dialog = FilterDialogFragment()
            val args = Bundle()
            args.putSerializable(TAGS_TAG, argTags.clone())
            dialog.arguments = args
            return dialog
        }

        const val TAGS_TAG = "tags"
        const val TAG = "FilterDialogFragment"
    }
}