package com.example.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.core.view.forEach
import com.example.fragments.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater)
        createListItems()
        return binding.root
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun createListItems() {
        with(binding) {
            (1..NUMBER_OF_ITEMS).forEach {
                val customItemView =
                    layoutInflater.inflate(R.layout.item_list, listItemsContainer, false)
                customItemView.findViewById<TextView>(R.id.fullName).text =
                    "${it}. ${customItemView.findViewById<TextView>(R.id.fullName).text}"
                Log.d(
                    "createListItems",
                    customItemView.findViewById<TextView>(R.id.fullName).text.toString()
                )
                customItemView.setOnClickListener {
                    onItemClick(
                        customItemView.findViewById<ImageView>(R.id.avatarImage).drawable.toBitmap(),
                        customItemView.findViewById<TextView>(R.id.fullName).text.toString(),
                        customItemView.findViewById<TextView>(R.id.description).text.toString()
                    )
                }
                listItemsContainer.addView(customItemView)
            }
        }
    }

    private fun onItemClick(imageBitMap: Bitmap, fullNameText: String, descriptionText: String) {
        Log.d("ListFragment", "$imageBitMap + $fullNameText + $descriptionText")
        (parentFragment as? ItemSelectListener)?.onItemSelected(
            imageBitMap,
            fullNameText,
            descriptionText
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val NUMBER_OF_ITEMS = 10

        fun newInstance() = ListFragment()
    }
}