package com.example.recyclerviewlist2

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.recyclerviewlist2.adapters.EmployeeAdapterDelegateHorizontal
import com.example.recyclerviewlist2.adapters.ManagerAdapterDelegateHorizontal
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager

class StaffAdapterWithBinding(
    onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer<Staff>(this, StaffDiffUtilCallback())

    private val delegatesManager = AdapterDelegatesManager<List<Staff>>()

    init {
        delegatesManager.addDelegate(ManagerAdapterDelegateHorizontal(onItemClicked))
            .addDelegate(EmployeeAdapterDelegateHorizontal(onItemClicked))
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(differ.currentList, position)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(differ.currentList, position, holder)
    }

    fun updateStaff(newStaff: List<Staff>) {
        differ.submitList(newStaff)
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