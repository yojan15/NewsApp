package com.example.newsapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.NewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.fragments.About
import com.example.newsapp.themeUtils.ThemeUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "News"


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
//
//        binding.ViewAllSavedNews.setOnClickListener {
//            val fragment = SavedNewsFragment()
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView, fragment)
//                .addToBackStack(null)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit()
        //}
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
                R.id.about -> "About"
                else -> "News"
            }
        }
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

    //    fun showFab() {
//        binding.floatingActionButton.show()
//        binding.ViewAllSavedNews.show()
//    }
//    fun hideFab() {
//        binding.floatingActionButton.hide()
//        binding.ViewAllSavedNews.hide()
//    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchApi(query)
                }

                val dayNightItem = menu.findItem(R.id.action_toggle_day_night)
                updateDayNightIcon(dayNightItem)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    searchApi(newText)
                }
                return true
            }
        })
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val dayNightItem = menu!!.findItem(R.id.action_toggle_day_night)
        updateDayNightIcon(dayNightItem)

        return super.onPrepareOptionsMenu(menu)
    }
    private fun searchApi(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(NewsApi::class.java)

        val call = apiService.searchNews(query)

        call.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.e("searching", result.toString())

                    val articles = result?.articles ?: emptyList()

                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    val adapter = NewsAdapter(object : NewsAdapter.OnItemClickListener {

                        override fun onTitleClick(article: Article) {}

                        override fun onImageOrDescriptionClick(article: Article) {}

                        override fun onTitleLongClick(article: Article, view: View): Boolean {
                            return true
                        }
                    })
                    recyclerView.adapter = adapter
                    adapter.submitList(articles)
                } else {
                    Log.e("searching", "failed")
                    Toast.makeText(this@MainActivity, "No Data Found..", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e("MainActivity", "$t")
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save ->  {
                navController.navigate(R.id.savedNewsFragment)
                // Toast.makeText(this,"saved Clicked",Toast.LENGTH_SHORT).show()
            }

            R.id.about -> {
                val fragment = About()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show()
            }

            R.id.action_toggle_day_night -> {
                toggleDayNightTheme()
                updateDayNightIcon(item)
                return true
            }
            else -> {
                binding.floatingActionButton.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleDayNightTheme() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val newNightMode = when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            Configuration.UI_MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(newNightMode)
        recreate()
    }

    private fun updateDayNightIcon(item: MenuItem) {
        val iconResId = if (ThemeUtils.isDarkTheme(this)) {
            R.drawable.baseline_mode_night_24
        } else {
            R.drawable.baseline_sunny_24
        }
        val actionView = item.actionView as? ImageView
        actionView?.apply {
            setImageResource(iconResId)
            setOnClickListener {
                toggleDayNightTheme()
                updateDayNightIcon(item)
            }
        }
    }
}