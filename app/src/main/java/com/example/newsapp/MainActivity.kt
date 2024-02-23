package com.example.newsapp

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.SavedNewsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News"

        // Initialize NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.ViewAllSavedNews.setOnClickListener {
            val fragment = SavedNewsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }

        val fab: FloatingActionButton = binding.floatingActionButton
        fab.setOnClickListener {
            showPopupMenu(fab)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.topHeadlines -> "Top Headlines"
                R.id.business -> "Business"
                R.id.entertainment -> "Entertainment"
                R.id.health -> "Health"
                R.id.sports -> "Sports"
                R.id.science -> "Science"
                R.id.technology -> "Technology"
                R.id.savedNewsFragment -> "Saved News"
                else -> "News"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the query submission
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the query text change
                return true
            }
        })

        return true
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_fab)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.topHeadlines -> {
                    navController.navigate(R.id.topHeadlines)
                    true
                }
                R.id.business -> {
                    navController.navigate(R.id.business)
                    true
                }
                R.id.entertainment -> {
                    navController.navigate(R.id.entertainment)
                    true
                }
                R.id.health -> {
                    navController.navigate(R.id.health)
                    true
                }
                R.id.sports -> {
                    navController.navigate(R.id.sports)
                    true
                }
                R.id.science -> {
                    navController.navigate(R.id.science)
                    true
                }
                R.id.technology -> {
                    navController.navigate(R.id.technology)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    fun showFab() {
        binding.floatingActionButton.show()
        binding.ViewAllSavedNews.show()
    }

    fun hideFab() {
        binding.floatingActionButton.hide()
        binding.ViewAllSavedNews.hide()
    }
}
