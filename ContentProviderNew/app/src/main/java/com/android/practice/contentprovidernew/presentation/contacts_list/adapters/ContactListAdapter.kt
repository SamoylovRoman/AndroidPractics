package com.android.practice.contentprovidernew.presentation.contacts_list.adapters

import androidx.recyclerview.widget.DiffUtil
import com.android.practice.contentprovidernew.presentation.view_objects.ContactVO
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class ContactListAdapter(
    onContactClick: (ContactVO) -> Unit
) : AsyncListDifferDelegationAdapter<ContactVO>(ContactDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(ContactAdapterDelegate(onContactClick))
    }

    class ContactDiffUtilCallback : DiffUtil.ItemCallback<ContactVO>() {
        override fun areItemsTheSame(oldItem: ContactVO, newItem: ContactVO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactVO, newItem: ContactVO): Boolean {
            return oldItem == newItem
        }
    }

}