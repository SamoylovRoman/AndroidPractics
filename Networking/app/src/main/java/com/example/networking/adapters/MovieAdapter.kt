package com.example.networking.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networking.R
import com.example.networking.data.Movie
import com.example.networking.databinding.ItemMovieBinding

class MovieAdapter : ListAdapter<Movie, MovieAdapter.MovieHolder>(DiffUtilCallback()) {

    class MovieHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            with(binding) {
                Glide.with(root)
                    .load(movie.iconLink)
                    .placeholder(R.drawable.ic_download)
                    .error(R.drawable.ic_error)
                    .into(iconMovieImage)
                root.setBackgroundResource(movie.color)
                titleMovieText.text = "${movie.title} (${movie.year})"
                genreMovieText.text = movie.genre
//                ratingMovieText.text = movie.rating
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(currentList[position])
    }
}