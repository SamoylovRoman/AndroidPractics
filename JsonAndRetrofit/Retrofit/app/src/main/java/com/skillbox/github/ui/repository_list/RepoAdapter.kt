package com.skillbox.github.ui.repository_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.github.R
import com.skillbox.github.data.RemoteRepo
import com.skillbox.github.databinding.ItemRepoBinding
import kotlin.coroutines.coroutineContext

class RepoAdapter(
    private val onItemClicked: (RemoteRepo) -> Unit
) :
    ListAdapter<RemoteRepo, RepoAdapter.MovieHolder>(DiffUtilCallback()) {

    inner class MovieHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(repo: RemoteRepo) {
            with(binding) {
                repoName.text = repo.name
                repoFullName.text = repo.fullName
                Glide.with(root)
                    .load(repo.owner.avatar)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_error)
                    .into(ownerImage)
                repoOwnerName.text =
                    root.context.getString(R.string.owner, repo.owner.ownerName)
                root.setOnClickListener {
                    onItemClicked(repo)
                }
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<RemoteRepo>() {
        override fun areItemsTheSame(oldItem: RemoteRepo, newItem: RemoteRepo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RemoteRepo, newItem: RemoteRepo): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(currentList[position])
    }
}