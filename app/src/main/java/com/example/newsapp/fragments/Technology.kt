package com.example.newsapp.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.TechnologyNewsApi
import com.example.newsapp.databinding.FragmentSportsBinding
import com.example.newsapp.databinding.FragmentTechnologyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class Technology : Fragment() {
    private lateinit var binding: FragmentTechnologyBinding
    private lateinit var newsAdapter : NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = FragmentTechnologyBinding.inflate(inflater, container, false)
        getNews()
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.technologyRecyclerView.adapter = newsAdapter
        binding.technologyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(TechnologyNewsApi::class.java)

        val retrofitData = retrofitBuilder.getNews()

    }
}