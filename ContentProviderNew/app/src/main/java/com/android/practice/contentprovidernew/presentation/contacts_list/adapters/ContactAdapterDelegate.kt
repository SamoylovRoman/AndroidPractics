package com.android.practice.contentprovidernew.presentation.contacts_list.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.practice.contentprovidernew.R
import com.android.practice.contentprovidernew.databinding.ItemContactBinding
import com.android.practice.contentprovidernew.presentation.extensions.inflate
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class ContactAdapterDelegate(
    private val onContactClick: (ContactVO) -> Unit
) : AbsListItemAdapterDelegate<ContactVO, ContactVO, ContactAdapterDelegate.Holder>() {

    override fun isForViewType(item: ContactVO, items: MutableList<ContactVO>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        return Holder(parent.inflate(R.layout.item_contact), onContactClick)
    }

    override fun onBindViewHolder(item: ContactVO, holder: Holder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class Holder(
        private val containerView: View,
        onContactClick: (ContactVO) -> Unit
    ) : RecyclerView.ViewHolder(containerView) {

        private var currentContact: ContactVO? = null
        private val binding: ItemContactBinding by viewBinding()

        init {
            containerView.setOnClickListener { currentContact?.let(onContactClick) }
        }

        fun bind(contact: ContactVO) {
            currentContact = contact
            binding.contactNameTextView.text = contact.name
            binding.contactPhoneTextView.text = contact.phoneNumbers.joinToString("\n")
        }

    }
}