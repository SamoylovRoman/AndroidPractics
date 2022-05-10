package com.example.permissionsanddate.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.example.permissionsanddate.Type
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class TypeAdapter(onItemClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<Type>(TypeDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(LocationAdapterDelegate(onItemClicked))
    }

    class TypeDiffUtilCallback : DiffUtil.ItemCallback<Type>() {
        override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
            Log.d("areItemsTheSame", "$oldItem $newItem")
            return when {
                oldItem is Type.Location && newItem is Type.Location -> oldItem.id == newItem.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem == newItem
        }
    }
}