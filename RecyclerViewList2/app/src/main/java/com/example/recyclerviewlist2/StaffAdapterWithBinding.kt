package com.example.recyclerviewlist2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerviewlist2.databinding.ItemEmployeeBinding
import com.example.recyclerviewlist2.databinding.ItemManagerBinding

class StaffAdapterWithBinding(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var staff: List<Staff> = emptyList()

//    abstract class CommonViewHolder():RecyclerView.ViewHolder(binding.root)

    inner class ManagerViewHolder(private val binding: ItemManagerBinding) :
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

    inner class EmployeeViewHolder(private val binding: ItemEmployeeBinding) :
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StaffAdapterWithBinding.ManagerViewHolder -> {
                val staff = staff[position].let { staff -> staff as? Staff.Manager }
                    ?: error(holder.itemView.resources.getString(R.string.text_manager_error) + position)
                holder.bind(staff)

            }
            is StaffAdapterWithBinding.EmployeeViewHolder -> {
                val staff = staff[position].let { staff -> staff as? Staff.Employee }
                    ?: error(holder.itemView.resources.getString(R.string.text_employee_error) + position)
                holder.bind(staff)
            }
            else -> error(holder.itemView.resources.getString(R.string.text_incorrect_view_holder) + holder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MANAGER -> ManagerViewHolder(
                ItemManagerBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            TYPE_EMPLOYEE -> EmployeeViewHolder(
                ItemEmployeeBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> error(parent.context.resources.getString(R.string.text_view_type_error) + viewType.toString())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (staff[position]) {
            is Staff.Manager -> TYPE_MANAGER
            else -> TYPE_EMPLOYEE
        }
    }

    fun updateStaff(newStaff: List<Staff>) {
        staff = newStaff
    }

    override fun getItemCount() = staff.size

    companion object {
        private const val TYPE_EMPLOYEE = 1
        private const val TYPE_MANAGER = 2
    }
}