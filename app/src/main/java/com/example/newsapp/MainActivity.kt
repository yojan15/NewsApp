package com.example.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "News"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        checkAndShowPopupMenu()
    }
    private fun checkAndShowPopupMenu() {
        if (isNetworkConnected()) {
            val fab: FloatingActionButton = binding.floatingActionButton
            fab.setOnClickListener {
                showPopupMenu(fab)
            }
        } else {
            showNoInternetDialog()
        }
    }
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
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
    private fun showNoInternetDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { _, _ ->
                checkAndShowPopupMenu()
            }
            .setNegativeButton("Exit") { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }
//    private fun openFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
}