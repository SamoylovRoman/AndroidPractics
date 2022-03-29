package com.example.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.fragments.databinding.FragmentListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val itemSelectListener: ItemSelectListener?
        get() = parentFragment?.let { fragment -> fragment as? ItemSelectListener }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater)
        createListItems()
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun createListItems() {
        (1..N_OF_ITEMS).forEach {
            with(binding) {
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
                        customItemView.findViewById<ImageView>(R.id.avatarImage).toString(),
                        customItemView.findViewById<TextView>(R.id.fullName).text.toString(),
                        customItemView.findViewById<TextView>(R.id.description).text.toString()
                    )
                }
                listItemsContainer.addView(customItemView)
            }
        }
    }

    private fun onItemClick(imageText: String, fullNameText: String, descriptionText: String) {
        Log.d("ListFragment", "$imageText + $fullNameText + $descriptionText")
        (parentFragment as? ItemSelectListener)?.onItemSelected(
            imageText,
            fullNameText,
            descriptionText
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(str: String) {
        Toast.makeText(
            context,
            str,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val N_OF_ITEMS = 10


/*        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }*/
    }
}