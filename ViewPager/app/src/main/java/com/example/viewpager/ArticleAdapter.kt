package com.example.viewpager

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ArticleAdapter(
    private var articles: List<Article>,
    fragment:MainFragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun createFragment(position: Int): Fragment {
        val article: Article = articles[position]
        return ArticleFragment.newInstance(article.title, article.picture, article.text)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateArticlesByList (newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = articles[position]
}