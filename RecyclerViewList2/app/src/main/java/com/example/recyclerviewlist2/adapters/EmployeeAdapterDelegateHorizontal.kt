package com.example.recyclerviewlist2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewlist2.R
import com.example.recyclerviewlist2.Staff
import com.example.recyclerviewlist2.databinding.ItemEmployeeHorizontalBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class EmployeeAdapterDelegateHorizontal(private val onItemClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<Staff.Employee, Staff, EmployeeAdapterDelegateHorizontal.EmployeeViewHolder>() {

    override fun isForViewType(item: Staff, items: MutableList<Staff>, position: Int): Boolean {
        return item is Staff.Employee
    }

    override fun onCreateViewHolder(parent: ViewGroup): EmployeeViewHolder {
        Log.d("onCreateViewHolder", parent.findViewById<RecyclerView>(R.id.staffList).layoutManager.toString())
        return EmployeeViewHolder(
            ItemEmployeeHorizontalBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        item: Staff.Employee,
        holder: EmployeeViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    inner class EmployeeViewHolder(private val binding: ItemEmployeeHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.managementTeamText.isVisible = false
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(staff: Staff.Employee) {
            binding.fullNameText.text = staff.fullName
            binding.positionText.text = staff.position
            binding.phoneNumberText.text = staff.phoneNumber
            Glide.with(binding.root)
                .load(staff.photoLink)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(binding.photoImage)
        }
    }
}