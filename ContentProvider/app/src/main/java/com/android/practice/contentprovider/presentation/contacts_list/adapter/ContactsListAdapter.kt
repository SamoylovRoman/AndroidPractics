package com.android.practice.contentprovider.presentation.contacts_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.contentprovider.databinding.ContactItemBinding
import com.android.practice.contentprovider.presentation.view_objects.ContactInListVO

class ContactsListAdapter(
    private val onItemClicked: (Long) -> Unit
) :
    ListAdapter<ContactInListVO, ContactsListAdapter.ViewHolder>(DiffUtilCallback()) {

    inner class ViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: ContactInListVO) {
            with(binding) {
                contactName.text = "${contact.name}"
                phoneNumber.text = contact.phonesString
                root.setOnClickListener {
                    onItemClicked(contact.id)
                }
            }
            Log.d("AAA", "Bind: ${contact.name}: ${contact.phonesString}")
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<ContactInListVO>() {
        override fun areItemsTheSame(oldItem: ContactInListVO, newItem: ContactInListVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactInListVO,
            newItem: ContactInListVO
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}