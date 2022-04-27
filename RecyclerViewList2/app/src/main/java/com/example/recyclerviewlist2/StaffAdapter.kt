package com.example.recyclerviewlist2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StaffAdapter(
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var staff: List<Staff> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MANAGER -> ManagerHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_manager_horizontal, parent, false), onItemClicked
            )
            TYPE_EMPLOYEE -> EmployeeHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_employee_horizontal, parent, false), onItemClicked
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ManagerHolder -> {
                val staff = staff[position].let { staff -> staff as? Staff.Manager }
                    ?: error(holder.itemView.resources.getString(R.string.text_manager_error) + position)
                holder.bind(staff)

            }
            is EmployeeHolder -> {
                val staff = staff[position].let { staff -> staff as? Staff.Employee }
                    ?: error(holder.itemView.resources.getString(R.string.text_employee_error) + position)
                holder.bind(staff)
            }
            else -> error(holder.itemView.resources.getString(R.string.text_incorrect_view_holder) + holder)
        }
    }

    override fun getItemCount(): Int = staff.size

    fun updateStaff(newStaff: List<Staff>) {
        staff = newStaff
    }

    abstract class BaseStaffHolder(
        view: View,
        onItemClicked: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val fullNameTextView: TextView = view.findViewById(R.id.fullNameText)
        private val positionTextView: TextView = view.findViewById(R.id.positionText)
        private val phoneNumberTextView: TextView = view.findViewById(R.id.phoneNumberText)
        private val photoImageView: ImageView = view.findViewById(R.id.photoImage)

        init {
            view.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        protected fun bindMainInfo(
            fullName: String,
            position: String,
            phoneNumber: String,
            photoLink: String
        ) {
            fullNameTextView.text = fullName
            positionTextView.text = position
            phoneNumberTextView.text = phoneNumber

            Glide.with(itemView)
                .load(photoLink)
                .placeholder(R.drawable.ic_download)
                .error(R.drawable.ic_error)
                .into(photoImageView)
        }
    }

    class EmployeeHolder(
        view: View,
        onItemClicked: (position: Int) -> Unit
    ) : BaseStaffHolder(view, onItemClicked) {
        init {
            view.findViewById<TextView>(R.id.managementTeamText).isVisible = false
        }

        fun bind(staff: Staff.Employee) {
            bindMainInfo(staff.fullName, staff.position, staff.phoneNumber, staff.photoLink)
        }
    }

    class ManagerHolder(
        view: View,
        onItemClicked: (position: Int) -> Unit
    ) : BaseStaffHolder(view, onItemClicked) {
        init {
            view.findViewById<TextView>(R.id.managementTeamText).isVisible = true
        }

        private val emailAddressView: TextView = view.findViewById(R.id.emailAddressText)

        fun bind(staff: Staff.Manager) {
            bindMainInfo(staff.fullName, staff.position, staff.phoneNumber, staff.photoLink)
            emailAddressView.text = staff.emailAddress
        }
    }

    companion object {
        private const val TYPE_EMPLOYEE = 1
        private const val TYPE_MANAGER = 2
    }
}