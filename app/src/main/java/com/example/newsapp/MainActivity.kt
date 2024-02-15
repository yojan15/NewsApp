package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.Business
import com.example.newsapp.fragments.Entertainment
import com.example.newsapp.fragments.Health
import com.example.newsapp.fragments.Science
import com.example.newsapp.fragments.Sports
import com.example.newsapp.fragments.Technology
import com.example.newsapp.fragments.TopHeadlines
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        * call FloatingActionButton from xml and add onClickListener to popUp
        * sub menus of FAB*/
        val fab : FloatingActionButton = binding.floatingActionButton
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
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}

