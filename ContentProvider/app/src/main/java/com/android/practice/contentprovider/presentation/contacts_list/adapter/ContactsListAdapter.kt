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
    private val onItemClick: (Long) -> Unit
) :
    ListAdapter<ContactInListVO, ContactsListAdapter.Holder>(ContactDiffUtilCallback) {

    private object ContactDiffUtilCallback : DiffUtil.ItemCallback<ContactInListVO>() {
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

    class Holder(
        private val binding: ContactItemBinding,
        private val onItemClicked: (Long) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private var personId: Long? = null

        init {
            binding.root.setOnClickListener { _ ->
                personId?.let {
                    onItemClicked(it)
                }
            }
        }

        fun bind(contact: ContactInListVO) {
            personId = contact.id
            with(binding) {
                contactName.text = "${contact.name}"
                phoneNumber.text = contact.phonesString

            }
            Log.d("AAA", "Bind: ${contact.name}: ${contact.phonesString}")
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            ContactItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(currentList[position])
    }
}