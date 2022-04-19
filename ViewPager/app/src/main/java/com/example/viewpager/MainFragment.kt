package com.example.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager.databinding.FragmentMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import java.io.Serializable
import kotlin.random.Random

class MainFragment : Fragment(), FilterDialogCallBack {

    private val articles: List<Article> = listOf(
        Article(
            title = R.string.article_title_text_1,
            picture = R.drawable.jpg_article_1,
            text = R.string.article_text_1,
            tags = listOf(ArticleTag.WORLD, ArticleTag.HEALTH)
        ),
        Article(
            title = R.string.article_title_text_2,
            picture = R.drawable.jpg_article_2,
            text = R.string.article_text_2,
            tags = listOf(ArticleTag.TECHNOLOGY)
        ),
        Article(
            title = R.string.article_title_text_3,
            picture = R.drawable.jpg_article_3,
            text = R.string.article_text_3,
            tags = listOf(ArticleTag.WORLD)
        ),
        Article(
            title = R.string.article_title_text_4,
            picture = R.drawable.jpg_article_4,
            text = R.string.article_text_4,
            tags = listOf(ArticleTag.SPORT, ArticleTag.ART, ArticleTag.POLITICS)
        ),
        Article(
            title = R.string.article_title_text_5,
            picture = R.drawable.jpg_article_5,
            text = R.string.article_text_5,
            tags = listOf(
                ArticleTag.BUSINESS,
                ArticleTag.POLITICS,
                ArticleTag.SPORT,
                ArticleTag.HEALTH
            )
        )
    )

    private var articleTagsToShow = BooleanArray(ArticleTag.values().size) { true }
    private lateinit var adapter: ArticleAdapter
    private lateinit var dialog: FilterDialogFragment


    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onOKButtonClick(booleanArray: BooleanArray) {
        updateArticles(booleanArray)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            articleTagsToShow =
                savedInstanceState.getSerializable(ARTICLE_TAGS) as BooleanArray
        }
        initViewPager()
        initIndicator()
        initTabLayoutMediator()
        initListeners()
        if (savedInstanceState != null) {
            val badgesList =
                savedInstanceState.getSerializable(BADGE_TAGS) as IntArray
            initTabLayoutBadges(badgesList)
        }
    }

    private fun initListeners() {
        binding.buttonGenerateEvent.setOnClickListener {
            createRandomBadge()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)?.removeBadge()
            }
        })

        binding.toolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemFilter -> {
                    showFilterDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun showFilterDialog() {
        dialog = FilterDialogFragment.newInstance(articleTagsToShow)
        dialog.show(childFragmentManager, FilterDialogFragment.TAG)
    }

    private fun initViewPager() {
        adapter = ArticleAdapter(articles, this)
        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(FanTransformation())
        if (articleTagsToShow.count { tag -> tag } != ArticleTag.values().size) {
            updateArticles(articleTagsToShow)
        }
    }

    private fun updateArticles(tags: BooleanArray) {
        val newTagsList: MutableList<ArticleTag> = emptyList<ArticleTag>().toMutableList()
        articleTagsToShow = tags
        ArticleTag.values().forEachIndexed { index, articleTag ->
            if (articleTagsToShow[index]) {
                newTagsList += articleTag
            }
        }
        val newArticles = articles.filter { article ->
            newTagsList.any { articleTag ->
                article.tags.contains(articleTag)
            }
        }
        adapter.updateArticlesByList(newArticles)
    }

    private fun createRandomBadge() {
        binding.tabLayout.getTabAt(Random.nextInt(articles.size))?.orCreateBadge?.apply {
            if (number > 0) {
                number++
            } else number = 1
            badgeGravity = BadgeDrawable.TOP_END
        }
    }

    private fun initTabLayoutBadges(badgesList: IntArray) {
        articles.forEachIndexed { index, _ ->
            if (badgesList[index] > 0) {
                binding.tabLayout.getTabAt(index)?.orCreateBadge?.apply {
                    number = badgesList[index]
                    badgeGravity = BadgeDrawable.TOP_END
                }
            }
        }
    }

    private fun getBudgesList(): IntArray {
        val badgesList = List(articles.size) { 0 }.toIntArray()
        articles.forEachIndexed { index, _ ->
            binding.tabLayout.getTabAt(index)?.badge?.apply {
                badgesList[index] = number
            }
        }
        return badgesList
    }

    private fun initIndicator() {
        binding.springDotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text =
                (getText(adapter.getItem(position).title).substring(0, MAX_LENGTH_OF_TITLE) + "...")
        }.attach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(ARTICLE_TAGS, articleTagsToShow as Serializable)
        outState.putSerializable(FilterDialogFragment.TAGS_TAG, articleTagsToShow as Serializable)
        outState.putSerializable(BADGE_TAGS, getBudgesList())
    }

    companion object {
        const val MAX_LENGTH_OF_TITLE = 15
        const val ARTICLE_TAGS = "Article tags"
        const val BADGE_TAGS = "Tab layout badges"

        fun newInstance() = MainFragment()
    }
}