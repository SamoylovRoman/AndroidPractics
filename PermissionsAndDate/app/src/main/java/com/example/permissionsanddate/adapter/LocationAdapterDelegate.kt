package com.example.permissionsanddate.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.permissionsanddate.R
import com.example.permissionsanddate.Type
import com.example.permissionsanddate.databinding.ItemLocationBinding
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class LocationAdapterDelegate(private val onItemClicked: (position: Int) -> Unit) :
    AbsListItemAdapterDelegate<Type.Location, Type, LocationAdapterDelegate.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val formatter = DateTimeFormatter.ofPattern("HH:mm dd:MM:yyyy")
            .withZone(ZoneId.systemDefault())

        init {
            binding.root.setOnClickListener {
                onItemClicked(bindingAdapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(location: Type.Location) {
            with(binding) {
                Glide.with(root)
                    .load(location.imageLink)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_error)
                    .into(pictureImage)
                locationText.text =
                    "Широта: ${location.latitude}, долгота: ${location.longitude}, скорость: ${location.speed}"
                dateTimeOfLocation.text = formatter.format(location.createdAt)
            }
        }
    }

    override fun isForViewType(item: Type, items: MutableList<Type>, position: Int): Boolean {
        return item is Type.Location
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        item: Type.Location,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.bind(item)
    }
}