package com.example.toolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.toolbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val users = listOf(
        "User 1",
        "User 2",
        "User 12",
        "Unknown 2",
        "User 323",
        "User 771",
        "User 122",
        "Unknown"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbarListeners()
    }

    private fun initToolbarListeners() {
        binding.toolbar.setNavigationOnClickListener {
            showToast(getString(R.string.text_navigation_clicked))
        }
        initSearchItemListener()
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItemInfo -> {
                    showToast(getString(R.string.text_about_app))
                    true
                }
                R.id.menuItemSearch -> {
                    true
                }
                R.id.menuItemSimple -> {
                    showToast(getString(R.string.text_simple_menu_item))
                    true
                }
                else -> false
            }
        }

    }

    private fun initSearchItemListener() {
        binding.toolbar.menu.findItem(R.id.menuItemSearch)
            .setOnActionExpandListener(object :
                MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    binding.textViewMain.text = getString(R.string.text_expanded)
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    showToast(getString(R.string.text_collapsed))
                    binding.textViewMain.text = getString(R.string.text_long_text)
                    return true
                }
            })

        (binding.toolbar.menu.findItem(R.id.menuItemSearch).actionView as? SearchView)
            ?.setOnQueryTextListener(
                object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        users.filter {
                            it.contains(
                                other = newText ?: "",
                                ignoreCase = true
                            )
                        }
                            .joinToString("\n")
                            .let {
                                binding.textViewMain.text = it
                            }
                        return true
                    }
                })
    }

    private fun showToast(str: String) {
        Toast.makeText(
            applicationContext,
            str,
            Toast.LENGTH_SHORT
        ).show()
    }
}