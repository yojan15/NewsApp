package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.Business
import com.example.newsapp.fragments.Entertainment
import com.example.newsapp.fragments.Health
import com.example.newsapp.fragments.Science
import com.example.newsapp.fragments.Sports
import com.example.newsapp.fragments.Technology
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
        val popupMenu = PopupMenu(this,view)
        popupMenu.inflate(R.menu.menu_fab)


        popupMenu.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.business -> {
                    openFragment(Business())
                    true
                }
                R.id.entertainment -> {
                    openFragment(Entertainment())
                    true
                }
                R.id.health -> {
                    openFragment(Health())
                    true
                }
                R.id.sports -> {
                    openFragment(Sports())
                    true
                }
                R.id.science -> {
                    openFragment(Science())
                    true
                }
                R.id.technology -> {
                    openFragment(Technology())
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .addToBackStack(null)
            .commit()
    }
}

