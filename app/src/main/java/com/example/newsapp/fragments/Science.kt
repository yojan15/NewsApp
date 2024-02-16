package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.ScienceNewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.databinding.FragmentScienceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Science : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var binding : FragmentScienceBinding
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScienceBinding.inflate(inflater, container, false)
        getNews()
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.scienceRecyclerView.adapter = newsAdapter
        binding.scienceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(ScienceNewsApi::class.java)

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                if(response.isSuccessful) {
                    val newsResponse = response.body()
                    if(newsResponse != null) {
                        val articles = newsResponse.articles

                        Log.e("MainActivity","Response code : ${response.code()}")
                        Log.e("MainActivity","Number of articles : ${articles.size}")

                        newsAdapter.submitList(articles)
                    } else {
                        Log.e("MainActivity", "news response is null")
                    }
                } else {
                    Log.e("MainActivity","Error ${response.message()}")
                    Log.e("MainActivity","Error body ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e("MainActivity","$t")
            }

        })
    }
    override fun onItemClick(article: Article) {
//        val action = ScienceDirections.actionScience2ToFullNewsFragment(article)
//        findNavController().navigate(action)

        try {
            val navController = findNavController()

            Log.d("BusinessFragment", "Item clicked: ${article.title}")
            Log.d("BusinessFragment", "NavController: $navController")

            val action = ScienceDirections.actionScience2ToFullNewsFragment(article)
            navController.navigate(action)
        } catch (exception: Exception) {
            Log.e("BusinessFragment", "Error navigating to FullNewsFragment", exception)
        }
    }
}