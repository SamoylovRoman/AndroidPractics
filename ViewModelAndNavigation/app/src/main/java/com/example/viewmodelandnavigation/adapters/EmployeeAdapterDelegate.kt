package com.example.viewmodelandnavigation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.viewmodelandnavigation.R
import com.example.viewmodelandnavigation.Staff
import com.example.viewmodelandnavigation.databinding.ItemEmployeeHorizontalBinding
import com.example.viewmodelandnavigation.databinding.ItemEmployeeVerticalBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class EmployeeAdapterDelegate(
    private val onItemClicked: (id: Long) -> Unit,
    private val onItemLongClicked: (position: Int) -> Unit
) :
    AbsListItemAdapterDelegate<Staff.Employee, Staff, EmployeeAdapterDelegate.ViewHolder>() {
    override fun isForViewType(item: Staff, items: MutableList<Staff>, position: Int): Boolean {
        return item is Staff.Employee
    }

    override fun onCreateViewHolder(parent: ViewGroup): EmployeeAdapterDelegate.ViewHolder {
        if (parent.findViewById<RecyclerView>(R.id.staffList).layoutManager is GridLayoutManager ||
            parent.findViewById<RecyclerView>(R.id.staffList).layoutManager is StaggeredGridLayoutManager
        ) {
            return ViewHolder(
                ItemEmployeeVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            ItemEmployeeHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: Staff.Employee,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(staff: Staff.Employee) {
            binding.root.setOnClickListener {
                onItemClicked(staff.id)
            }
            binding.root.setOnLongClickListener {
                onItemLongClicked(bindingAdapterPosition)
                true
            }
            when (binding) {
                is ItemEmployeeHorizontalBinding -> {
                    binding.managementTeamText.isVisible = false
                    binding.fullNameText.text = staff.fullName
                    binding.positionText.text = staff.position
                    binding.phoneNumberText.text = staff.phoneNumber
                    Glide.with(binding.root)
                        .load(staff.photoLink)
                        .placeholder(R.drawable.ic_download)
                        .error(R.drawable.ic_error)
                        .into(binding.photoImage)
                }
                is ItemEmployeeVerticalBinding -> {
                    binding.managementTeamText.isVisible = false
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
    }
}