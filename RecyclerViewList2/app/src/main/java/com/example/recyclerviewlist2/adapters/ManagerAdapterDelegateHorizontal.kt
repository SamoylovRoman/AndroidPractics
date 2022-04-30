package com.example.recyclerviewlist2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewlist2.R
import com.example.recyclerviewlist2.Staff
import com.example.recyclerviewlist2.databinding.ItemManagerHorizontalBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class ManagerAdapterDelegateHorizontal(
    private val onItemClicked: (position: Int) -> Unit
) :
    AbsListItemAdapterDelegate<Staff.Manager, Staff, ManagerAdapterDelegateHorizontal.ManagerViewHolder>() {

    override fun isForViewType(item: Staff, items: MutableList<Staff>, position: Int): Boolean {
        return item is Staff.Manager
    }

    override fun onCreateViewHolder(parent: ViewGroup): ManagerViewHolder {
        return ManagerViewHolder(
            ItemManagerHorizontalBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        item: Staff.Manager,
        holder: ManagerViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }

    inner class ManagerViewHolder(private val binding: ItemManagerHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.managementTeamText.isVisible = true
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        fun bind(staff: Staff.Manager) {
            binding.fullNameText.text = staff.fullName
            binding.positionText.text = staff.position
            binding.phoneNumberText.text = staff.phoneNumber
            Glide.with(binding.root)
                .load(staff.photoLink)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(binding.photoImage)
            binding.emailAddressText.text = staff.emailAddress
        }
    }
}