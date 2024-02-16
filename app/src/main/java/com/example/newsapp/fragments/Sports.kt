package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.SportsNewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.databinding.FragmentSportsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Sports : Fragment(), NewsAdapter.OnItemClickListener {
private lateinit var binding : FragmentSportsBinding
private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSportsBinding.inflate(inflater, container, false)
        getNews()
        setupRecyclerView()
        setupSwipeRefreshLayout()
        return binding.root
    }
    private fun setupSwipeRefreshLayout() {
        binding.sportsSwipeRefreshLayout.setOnRefreshListener {
            getNews()
        }
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.sportsRecyclerView.adapter = newsAdapter
        binding.sportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(SportsNewsApi::class.java)

        binding.sportsSwipeRefreshLayout.isRefreshing = false

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

                    Log.e("MainActivity", "Error Body: ${response.errorBody()?.string()}")
                }
            }
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e("MainActivity", "Failed to get news", t)
            }

        })
    }

    override fun onItemClick(article: Article) {
//        val action = SportsDirections.actionSports2ToFullNewsFragment(article)
//        findNavController().navigate(action)

        try {
            val navController = findNavController()

            Log.d("BusinessFragment", "Item clicked: ${article.title}")
            Log.d("BusinessFragment", "NavController: $navController")

            val action = SportsDirections.actionSportsToFullNewsFragment(article)
            navController.navigate(action)
        } catch (exception: Exception) {
            Log.e("BusinessFragment", "Error navigating to FullNewsFragment", exception)
        }
    }
}