package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.NewsApi
import com.example.newsapp.data.News
import com.example.newsapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var newsAdapter : NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        getNews()
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.adapter = newsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(NewsApi::class.java)

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        val articles = newsResponse.articles

                        Log.d("MainActivity", "Response code: ${response.code()}")
                        Log.d("MainActivity", "Number of articles: ${articles.size}")

                        newsAdapter.submitList(articles) // Update the adapter with the new list
                    } else {
                        Log.e("MainActivity", "News response is null")
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.message()}")
                    // Additional logging for debugging
                    Log.e("MainActivity", "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e("MainActivity", "Failed to get news", t)
            }

        })
    }
}