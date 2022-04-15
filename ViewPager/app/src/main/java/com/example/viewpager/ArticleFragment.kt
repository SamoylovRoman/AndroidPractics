package com.example.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.viewpager.databinding.FragmentArticleBinding

private const val ARG_ARTICLE_TITLE = "articleTitle"
private const val ARG_ARTICLE_DRAWABLE = "articleDrawable"
private const val ARG_ARTICLE_TEXT = "articleText"

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private var articleTitle: Int? = null
    private var articleDrawable: Int? = null
    private var articleText: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleTitle = it.getInt(ARG_ARTICLE_TITLE)
            articleDrawable = it.getInt(ARG_ARTICLE_DRAWABLE)
            articleText = it.getInt(ARG_ARTICLE_TEXT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTextView.text = getText(articleTitle!!)
        binding.pictureImage.setImageResource(articleDrawable!!)
        binding.textTextView.text = getText(articleText!!)
    }

    companion object {

        fun newInstance(
            @StringRes articleTitle: Int,
            @DrawableRes articleDrawable: Int,
            @StringRes articleText: Int
        ) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ARTICLE_TITLE, articleTitle)
                    putInt(ARG_ARTICLE_DRAWABLE, articleDrawable)
                    putInt(ARG_ARTICLE_TEXT, articleText)
                }
            }
    }
}