package com.example.newsapp.fragments

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

        val selectedArticle = arguments?.getParcelable<Article>("selectedArticle")

        selectedArticle?.let { article ->
            binding.fullNewsTitleTextView.text = article.title
            binding.fullNewsContentTextView.text = article.content

            Glide.with(requireContext())
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.fullNewsImageView)
        } ?: run {
           Log.e("FullNewsFragment","Null")
        }
    }
}