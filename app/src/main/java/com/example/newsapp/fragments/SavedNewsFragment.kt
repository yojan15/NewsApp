package com.example.newsapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.data.Article
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.viewModel.ArticleViewModel
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment(), NewsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var imageViewSorry: ImageView

    private val backStackListener = FragmentManager.OnBackStackChangedListener {
        updateFabVisibility()
    }

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

        articleViewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        newsAdapter = NewsAdapter(this)
        binding.recyclerviewSavedNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
        articleViewModel.allArticle.observe(viewLifecycleOwner) { articles ->
            Log.d("SavedNewsFragment", "Number of saved articles: ${articles.size}")
            displaySaved(articles)
        }
        imageViewSorry = binding.imageViewSorry

        // Register the back stack listener
        requireActivity().supportFragmentManager.addOnBackStackChangedListener(backStackListener)
    }

    override fun onDestroyView() {
        // Unregister the back stack listener to avoid memory leaks
        requireActivity().supportFragmentManager.removeOnBackStackChangedListener(backStackListener)
        super.onDestroyView()
    }

    private fun updateFabVisibility() {
        if (activity is MainActivity) {
            val mainActivity = activity as MainActivity
            val backStackEntryCount = requireActivity().supportFragmentManager.backStackEntryCount
            if (backStackEntryCount > 0) {
                // Fragment is in the back stack, hide the FAB
                mainActivity.hideFab()
            } else {
                // No fragments in the back stack, show the FAB
                mainActivity.showFab()
            }
        }
    }

    private fun displaySaved(articles: List<Article>?) {
        if (!articles.isNullOrEmpty()) {
            binding.recyclerviewSavedNews.visibility = View.VISIBLE
            imageViewSorry.visibility = View.GONE
            newsAdapter.submitList(articles)
        } else {
            binding.recyclerviewSavedNews.visibility = View.GONE
            imageViewSorry.visibility = View.VISIBLE
        }
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

    override fun onTitleLongClick(article: Article, view: View): Boolean {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
        startActivity(Intent.createChooser(shareIntent, "Share article URL"))
        return true
    }
}