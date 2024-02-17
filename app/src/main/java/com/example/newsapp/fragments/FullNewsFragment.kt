package com.example.newsapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.Article
import com.example.newsapp.databinding.FragmentFullNewsBinding

class FullNewsFragment : Fragment(){
    private lateinit var binding: FragmentFullNewsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFullNewsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fullNewsSwipeRefreshLayout.setOnRefreshListener {
                loadWebPage()
        }

        val selectedArticle = arguments?.getParcelable<Article>("selectedArticle")

        selectedArticle?.let { article ->
            binding.fullNewsTitleTextView.text = article.title
            binding.fullNewsContentTextView.text = article.content

            Glide.with(requireContext())
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.fullNewsImageView)

            loadWebPage()
//            binding.fullNewsUrl.text = article.url
//            binding.fullNewsUrl.setOnClickListener {
//                openWebPage(article.url)
//            }
        } ?: run {
           Log.e("FullNewsFragment","Null")
        }
    }
    private fun loadWebPage() {
        val selectedArticle = arguments?.getParcelable<Article>("selectedArticle")
        selectedArticle?.url?.let { url ->
            binding.webView.loadUrl(url)
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(intent)
        }
        binding.fullNewsSwipeRefreshLayout.isRefreshing = false
    }
    private fun openWebPage(url : String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}