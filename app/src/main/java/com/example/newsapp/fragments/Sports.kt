package com.example.newsapp.fragments

import android.content.Intent
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
import com.example.newsapp.api.SportsNewsApi
import com.example.newsapp.data.Article
import com.example.newsapp.data.News
import com.example.newsapp.data.toArticle
import com.example.newsapp.databinding.FragmentSportsBinding
import com.example.newsapp.viewModel.ArticleViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Sports : Fragment(), NewsAdapter.OnItemClickListener {
private lateinit var binding : FragmentSportsBinding
private lateinit var newsAdapter: NewsAdapter
private lateinit var articleViewModel: ArticleViewModel
private var savedArticles = mutableSetOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSportsBinding.inflate(inflater, container, false)
        setupRecyclerView()
        articleViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]


        articleViewModel.allCachedArticles.observe(viewLifecycleOwner) { cachedArticles ->
            /**
             * gets all the articles from cacheArticle table when user is not connected to internet
             */
            val articles = cachedArticles.map { it.toArticle() }
            newsAdapter.submitList(articles)

            // Now, observe saved articles and update the list
            articleViewModel.allArticle.observe(viewLifecycleOwner) { savedArticles ->
                Log.d("TopHeadlines", "Saved articles: $savedArticles")
                /**
                 * gets all the articles from Article table when user is connected to internet
                 */
                this.savedArticles.clear()
                this.savedArticles.addAll(savedArticles.map { it.url })
            }
        }
        getNews()   // calling the function to getNews
        setupSwipeRefreshLayout()
        return binding.root
    }

    private fun setupSwipeRefreshLayout() {
        binding.sportsSwipeRefreshLayout.setOnRefreshListener {
            /**
             * swipe down to get the latest news
             */
            getNews()

            articleViewModel.deleteAllCachedArticles()
            Log.e("cached articles","${articleViewModel.allCachedArticles}")
        }
    }

    private fun setupRecyclerView() {
        /**
         * set up RecyclerView to get all the article in recyclerView
         */
        newsAdapter = NewsAdapter(this)
        binding.sportsRecyclerView.adapter = newsAdapter
        binding.sportsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun getNews() {
        /**
         * create an instance of a retrofit to call the articles from base url
         */
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://newsapi.org/v2/")
            .build()
            .create(SportsNewsApi::class.java)
        binding.sportsSwipeRefreshLayout.isRefreshing = false
        articleViewModel.deleteAllCachedArticles()
        Log.e("cached articles","${articleViewModel.allCachedArticles}")

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

                        Log.d("com.example.newsapp.MainActivity", "Response code: ${response.code()}")
                        Log.d("com.example.newsapp.MainActivity", "Number of articles: ${newArticles.size}")

                        newArticles.forEach { article ->
                            lifecycleScope.launch {
                                val isSaved = articleViewModel.isArticleSaved(article.url).value
                                if (isSaved != null && isSaved) {
                                    // Delete all cached articles when a new article is saved
                                    articleViewModel.deleteAllCachedArticles()
                                    Toast.makeText(
                                        requireContext(), "Article removed from saved list", Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    articleViewModel.insert(article)
                                }
                            }
                            // Only submit the list of new articles to the adapter
                            newsAdapter.submitList(newArticles)
                        }
                    } else {
                        Log.e("com.example.newsapp.MainActivity", "News response is null")
                    }
                } else {
                    Log.e("com.example.newsapp.MainActivity", "Error: ${response.message()}")
                    Log.e("com.example.newsapp.MainActivity", "Error Body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.e("com.example.newsapp.MainActivity", "Failed to get news", t)
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
                            "Article removed from saved list", Toast.LENGTH_SHORT).show()
                    } else {
                        articleViewModel.saveArticle(article)
                        Toast.makeText(requireContext(), "Article saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error determining article status", Toast.LENGTH_SHORT).show()
                }
                isSavedLiveData.removeObservers(requireActivity())
            }
        }
    }
    override fun onImageOrDescriptionClick(article: Article) {
       val action = SportsDirections.actionSportsToFullNewsFragment(article)
        findNavController().navigate(action)
    }

    override fun onTitleLongClick(article: Article, view: View): Boolean {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
        startActivity(Intent.createChooser(shareIntent, "Share article URL"))
        return true
    }
}
