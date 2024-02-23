package com.example.newsapp.fragments

import android.os.Bundle
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
import com.example.newsapp.api.NewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.data.toArticle
import com.example.newsapp.databinding.FragmentTopHeadlinesBinding
import com.example.newsapp.viewModel.ArticleViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class TopHeadlines : Fragment(), NewsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentTopHeadlinesBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var articleViewModel: ArticleViewModel
    private val savedArticles = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopHeadlinesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        articleViewModel.allCachedArticles.observe(viewLifecycleOwner) { cachedArticles ->
            val articles = cachedArticles.map { it.toArticle() }
            newsAdapter.submitList(articles)

            // Now, observe saved articles and update the list
            articleViewModel.allArticle.observe(viewLifecycleOwner) { savedArticles ->
                Log.d("TopHeadlines", "Saved articles: $savedArticles")
                this.savedArticles.clear()
                this.savedArticles.addAll(savedArticles.map { it.url })
            }
        }
        getNews()
        setupSwipeRefreshLayout()
        return binding.root
    }

    private fun setupSwipeRefreshLayout() {
        binding.topHeadlinesSwipeRefreshLayout.setOnRefreshListener {
            getNews()
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.recyclerView.adapter = newsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getNews() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(NewsApi::class.java)
        binding.topHeadlinesSwipeRefreshLayout.isRefreshing = false

        val retrofitData = retrofitBuilder.getNews()
        retrofitData.enqueue(object : Callback<News> {

            override fun onResponse(call: Call<News>, response: Response<News>) {
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    if (newsResponse != null) {
                        val savedArticleUrls = articleViewModel.allArticle.value?.map { it.url } ?: emptyList()

                        val newArticles = newsResponse.articles.filter { article ->
                            article.url !in savedArticleUrls
                        }

                        Log.d("MainActivity", "Response code: ${response.code()}")
                        Log.d("MainActivity", "Number of articles: ${newArticles.size}")

                        newArticles.forEach { article ->
                            lifecycleScope.launch {
                                val isSaved = articleViewModel.isArticleSaved(article.url).value
                                if (isSaved != null && isSaved) {
                                    articleViewModel.deleteAllCachedArticles()
                                    Toast.makeText(
                                        requireContext(),
                                        "Article removed from saved list",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    articleViewModel.insert(article)
                                }
                            }
                        }
                        // Only submit the list of new articles to the adapter
                        newsAdapter.submitList(newArticles)
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

    override fun onTitleClick(article: Article) {
        lifecycleScope.launch {
            val isSavedLiveData = articleViewModel.isArticleSaved(article.url)
            isSavedLiveData.observe(requireActivity()) { isSaved ->
                if (isSaved != null) {
                    if (isSaved) {
                        articleViewModel.delete(article)
                        Toast.makeText(
                            requireContext(),
                            "Article removed from saved list",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        articleViewModel.saveArticle(article)
                        Toast.makeText(
                            requireContext(),
                            "Article saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error determining article status",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isSavedLiveData.removeObservers(requireActivity())
            }
        }
    }

    override fun onImageOrDescriptionClick(article: Article) {
        val action = TopHeadlinesDirections.actionTopHeadlinesToFullNewsFragment(article)
        findNavController().navigate(action)
    }
}
