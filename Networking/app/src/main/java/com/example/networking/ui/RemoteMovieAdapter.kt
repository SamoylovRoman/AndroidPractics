package com.example.networking.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networking.R
import com.example.networking.data.RemoteMovie
import com.example.networking.databinding.ItemMovieBinding

class RemoteMovieAdapter :
    ListAdapter<RemoteMovie, RemoteMovieAdapter.MovieHolder>(DiffUtilCallback()) {

    class MovieHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(movie: RemoteMovie) {
            with(binding) {
                Glide.with(root)
                    .load(movie.posterLink)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_error)
                    .into(iconMovieImage)
                root.setBackgroundResource(movie.itemColor)
                titleMovieText.text = "${movie.title} (${movie.year})"
                genreMovieText.text = "${movie.type} / ${movie.id}"
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<RemoteMovie>() {
        override fun areItemsTheSame(oldItem: RemoteMovie, newItem: RemoteMovie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RemoteMovie, newItem: RemoteMovie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(currentList[position])
    }
}