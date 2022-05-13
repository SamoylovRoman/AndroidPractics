package com.example.viewmodelandnavigation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.viewmodelandnavigation.Staff
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class StaffAdapter(
    onItemClicked: (id: Long) -> Unit,
    onItemLongClicked: (position: Int) -> Unit
) : AsyncListDifferDelegationAdapter<Staff>(StaffDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(ManagerAdapterDelegate(onItemClicked, onItemLongClicked))
            .addDelegate(EmployeeAdapterDelegate(onItemClicked, onItemLongClicked))
    }

    class StaffDiffUtilCallback : DiffUtil.ItemCallback<Staff>() {
        override fun areItemsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return when {
                oldItem is Staff.Manager && newItem is Staff.Manager ->
                    oldItem.id == newItem.id
                oldItem is Staff.Employee && newItem is Staff.Employee ->
                    oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Staff, newItem: Staff): Boolean {
            return oldItem == newItem
        }
    }
}