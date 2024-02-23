package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.data.Article
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewModel.ArticleViewModel
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment() , NewsAdapter.OnItemClickListener {
    private lateinit var binding : FragmentSavedNewsBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("SavedNewsFragment", "onViewCreated invoked")
        super.onViewCreated(view, savedInstanceState)
        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        newsAdapter = NewsAdapter(this)
        binding.recyclerviewSavedNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
        articleViewModel.allArticle.observe(viewLifecycleOwner, Observer { articles ->
            Log.d("SavedNewsFragment", "Number of saved articles: ${articles.size}")
            displaySaved(articles)
        })

    }

    private fun displaySaved(articles: List<Article>?) {
        newsAdapter.submitList(articles)
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
        val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToFullNewsFragment(article)
        findNavController().navigate(action)
    }
}