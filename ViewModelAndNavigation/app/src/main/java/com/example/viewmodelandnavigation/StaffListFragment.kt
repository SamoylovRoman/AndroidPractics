package com.example.viewmodelandnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.viewmodelandnavigation.adapters.StaffAdapter
import com.example.viewmodelandnavigation.databinding.FragmentStaffListBinding
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class StaffListFragment : Fragment() {
    private val staffViewModel: StaffViewModel by viewModels()

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = _binding!!

    private var staffAdapter: StaffAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStaffListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initFloatButtonListener()
        observeViewModelState()
    }

    private fun initList() {
        staffAdapter = StaffAdapter({ id ->
            val action = StaffListFragmentDirections.actionStaffListFragmentToStaffDetailsFragment(
                id,
                staffViewModel.getStaff(id)
            )
            findNavController().navigate(action)
        },
            { position ->
                deleteStaff(position)
            }/*,
            object : Runnable {
                override fun run() {
//                    binding.staffList.scrollToPosition(0)
                }
            }*/)
        with(binding.staffList) {
            adapter = staffAdapter
            initLinearLayout(this)
            setHasFixedSize(true)
            itemAnimator = SlideInLeftAnimator()
        }
        updateEmptyTextView()
    }

    private fun initLinearLayout(recyclerView: RecyclerView) {
        val linearLayout = LinearLayoutManager(context)
        with(recyclerView) {
            layoutManager = linearLayout
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initFloatButtonListener() {
        binding.addFloatButton.setOnClickListener {
            addStaff()
        }
    }

    private fun addStaff() {
        staffViewModel.addStaff()
//        binding.staffList.scrollToPosition(0)
        updateEmptyTextView()
    }

    private fun deleteStaff(position: Int) {
        staffViewModel.deleteStaff(position)
        updateEmptyTextView()
    }

    private fun updateEmptyTextView() {
        if (staffViewModel.isEmptyLiveData()) {
            binding.emptyListText.visibility = View.VISIBLE
        } else binding.emptyListText.visibility = View.GONE
    }

    private fun observeViewModelState() {
        staffViewModel.staff
            .observe(viewLifecycleOwner) { newStaffList ->
                staffAdapter?.setItems(newStaffList.toMutableList()) {
                    binding.staffList.scrollToPosition(0)
                }
                updateEmptyTextView()
            }
        staffViewModel.showToast
            .observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_item_added),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        staffAdapter = null
    }
}