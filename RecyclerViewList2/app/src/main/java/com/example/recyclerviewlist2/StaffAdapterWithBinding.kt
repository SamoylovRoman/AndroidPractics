package com.example.recyclerviewlist2

import androidx.recyclerview.widget.DiffUtil
import com.example.recyclerviewlist2.adapters.EmployeeAdapterDelegate
import com.example.recyclerviewlist2.adapters.ManagerAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class StaffAdapterWithBinding(
    onItemClicked: (position: Int) -> Unit
) : AsyncListDifferDelegationAdapter<Staff>(StaffDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(ManagerAdapterDelegate(onItemClicked))
            .addDelegate(EmployeeAdapterDelegate(onItemClicked))
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