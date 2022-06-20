package com.skillbox.github.ui.current_user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.github.R
import com.skillbox.github.data.RemoteFollowing
import com.skillbox.github.databinding.ItemFollowingBinding

class FollowingAdapter :
    ListAdapter<RemoteFollowing, FollowingAdapter.FollowingHolder>(DiffUtilCallback()) {

    inner class FollowingHolder(private val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(following: RemoteFollowing) {
            with(binding) {
                followingName.text = following.login
                Glide.with(root)
                    .load(following.avatarUrl)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_error)
                    .into(followingAvatar)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<RemoteFollowing>() {
        override fun areItemsTheSame(oldItem: RemoteFollowing, newItem: RemoteFollowing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RemoteFollowing, newItem: RemoteFollowing): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingHolder =
        FollowingHolder(ItemFollowingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FollowingHolder, position: Int) {
        holder.bind(currentList[position])
    }
}