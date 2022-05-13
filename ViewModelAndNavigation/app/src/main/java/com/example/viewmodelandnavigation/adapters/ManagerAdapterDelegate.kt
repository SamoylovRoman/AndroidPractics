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
import com.example.viewmodelandnavigation.databinding.ItemManagerHorizontalBinding
import com.example.viewmodelandnavigation.databinding.ItemManagerVerticalBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class ManagerAdapterDelegate(
    private val onItemClicked: (id: Long) -> Unit,
    private val onItemLongClicked: (position: Int) -> Unit
) :
    AbsListItemAdapterDelegate<Staff.Manager, Staff, ManagerAdapterDelegate.ViewHolder>() {
    override fun isForViewType(item: Staff, items: MutableList<Staff>, position: Int): Boolean {
        return item is Staff.Manager
    }

    override fun onCreateViewHolder(parent: ViewGroup): ManagerAdapterDelegate.ViewHolder {
        if (parent.findViewById<RecyclerView>(R.id.staffList).layoutManager is GridLayoutManager ||
            parent.findViewById<RecyclerView>(R.id.staffList).layoutManager is StaggeredGridLayoutManager
        ) {
            return ViewHolder(
                ItemManagerVerticalBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ViewHolder(
            ItemManagerHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        item: Staff.Manager,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(staff: Staff.Manager) {
            binding.root.setOnClickListener {
                onItemClicked(staff.id)
            }

            binding.root.setOnLongClickListener {
                onItemLongClicked(bindingAdapterPosition)
                true
            }
            when (binding) {
                is ItemManagerHorizontalBinding -> {
                    binding.managementTeamText.isVisible = true
                    binding.fullNameText.text = staff.fullName
                    binding.positionText.text = staff.position
                    binding.phoneNumberText.text = staff.phoneNumber
                    binding.emailAddressText.text = staff.emailAddress
                    Glide.with(binding.root)
                        .load(staff.photoLink)
                        .placeholder(R.drawable.ic_download)
                        .error(R.drawable.ic_error)
                        .into(binding.photoImage)
                }
                is ItemManagerVerticalBinding -> {
                    binding.managementTeamText.isVisible = true
                    binding.fullNameText.text = staff.fullName
                    binding.positionText.text = staff.position
                    binding.phoneNumberText.text = staff.phoneNumber
                    binding.emailAddressText.text = staff.emailAddress
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