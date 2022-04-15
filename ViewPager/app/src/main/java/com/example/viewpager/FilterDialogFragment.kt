package com.example.viewpager

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class FilterDialogFragment : DialogFragment() {

    private lateinit var tags: BooleanArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tags = arguments?.getSerializable(TAGS_TAG) as BooleanArray
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tagsArray: List<String> = ArticleTag.values().map { tag ->
            tag.toString()
        }
        return AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.text_dialog_caption))
            .setPositiveButton(getString(R.string.text_positive_button)) { _, _ ->
                (parentFragment as FilterDialogCallBack).onOKButtonClick()
            }
            .setMultiChoiceItems(
                // I think to use instead tagsArray in this fun
                // ArticleTag.values().map { tag -> tag.toString() }.toTypedArray(),
                tagsArray.toTypedArray(),
                tags
            ) { _, which, isChecked ->
                tags[which] = isChecked
            }
            .create()
    }

    companion object {
        const val TAGS_TAG = "tags"
        const val TAG = "FilterDialogFragment"
    }
}