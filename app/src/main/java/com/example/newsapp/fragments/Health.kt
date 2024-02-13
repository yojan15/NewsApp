package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.HealthNewsApi
import com.example.newsapp.data.News
import com.example.newsapp.databinding.FragmentHealthBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Health : Fragment() {
    private lateinit var binding : FragmentHealthBinding
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHealthBinding.inflate(inflater, container, false)
        getNews()
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.healthRecyclerView.adapter = newsAdapter
        binding.healthRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(HealthNewsApi::class.java)

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if(response.isSuccessful) {
                    val newsResponse = response.body()
                    if(newsResponse != null) {
                        val articles = newsResponse.articles

                        Log.e("MainActivity","Response code : ${response.code()}")
                        Log.e("MainActivity","Number of Article : ${articles.size}")

                        newsAdapter.submitList(articles)
                    } else {
                        Log.e("MainActivity","Error : ${response.message()}")
                        Log.e("MainActivity","Error Body : ${response.errorBody().toString()}")
                    }
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
            Log.e("MainActivity","$t")
            }

        })
    }
}