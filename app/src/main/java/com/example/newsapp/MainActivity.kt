package com.example.newsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.SavedNewsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.ViewAllSavedNews.setOnClickListener {
            val fragment = SavedNewsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,fragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }

        val fab: FloatingActionButton = binding.floatingActionButton
        fab.setOnClickListener {
            showPopupMenu(fab)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_fab)

        popupMenu.setOnMenuItemClickListener { item ->
            val navController = findNavController(R.id.fragmentContainerView)

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
}