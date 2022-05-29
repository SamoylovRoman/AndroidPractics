package com.example.moshi.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.moshi.data.Score
import com.example.moshi.databinding.ItemScoreBinding

class ScoreAdapter :
    ListAdapter<Score, ScoreAdapter.MovieHolder>(DiffUtilCallback()) {

    class MovieHolder(private val binding: ItemScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(score: Score) {
            with(binding) {
                sourceName.text = score.source
                sourceValue.text = score.value
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Score>() {
        override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem.source == newItem.source
        }

        override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder =
        MovieHolder(ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(currentList[position])
    }
}