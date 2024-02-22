package com.example.newsapp.fragments

import android.os.Bundle
import android.provider.SyncStateContract.Helpers
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.HealthNewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.databinding.FragmentHealthBinding
import com.example.newsapp.viewModel.ArticleViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Health : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var binding : FragmentHealthBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var articleViewModel: ArticleViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHealthBinding.inflate(inflater, container, false)
        setupRecyclerView()
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        setupSwipeRefresh()
        return binding.root
    }

    private fun setupSwipeRefresh() {
        binding.healthSwipeRefreshLayout.setOnRefreshListener {
            getNews()
        }
    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.healthRecyclerView.adapter = newsAdapter
        binding.healthRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(HealthNewsApi::class.java)

        binding.healthSwipeRefreshLayout.isRefreshing = false

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if(response.isSuccessful) {
                    val newsResponse = response.body()
                    if(newsResponse != null) {
                        val articles = newsResponse.articles

                        Log.e("com.example.newsapp.MainActivity","Response code : ${response.code()}")
                        Log.e("com.example.newsapp.MainActivity","Number of Article : ${articles.size}")

                        newsAdapter.submitList(articles)
                    } else {
                        Log.e("com.example.newsapp.MainActivity","Error : ${response.message()}")
                        Log.e("com.example.newsapp.MainActivity","Error Body : ${response.errorBody().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
            Log.e("com.example.newsapp.MainActivity","$t")
            }

        })
    }
    override fun onTitleClick(article: Article) {
        lifecycleScope.launch {
            val isSavedLiveData = articleViewModel.isArticleSaved(article.url)

            // Observe the LiveData to get the value
            isSavedLiveData.observe(requireActivity()) { isSaved ->
                if (isSaved != null) {
                    if (isSaved) {
                        articleViewModel.delete(article)
                        Toast.makeText(requireContext(), "Article removed from saved list", Toast.LENGTH_SHORT).show()
                    } else {
                        articleViewModel.insert(article)
                        Toast.makeText(requireContext(), "Article saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the case when isArticleSaved LiveData is null
                    Toast.makeText(requireContext(), "Error determining article status", Toast.LENGTH_SHORT).show()
                }

                // Remove the observer to prevent multiple callbacks
                isSavedLiveData.removeObservers(requireActivity())
            }
        }
    }

    override fun onImageOrDescriptionClick(article: Article) {
        val action = HealthDirections.actionHealthToFullNewsFragment(article)
        findNavController().navigate(action)
    }
}