package com.example.newsapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.databinding.FragmentAboutBinding

class About : Fragment()  {
    private lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false)
//        githubLink()
        return binding.root
    }
//    private fun githubLink() {
//        binding.github.setOnClickListener {
//            val githubUrl = Uri.parse("https://github.com/yojan15/NewsApp")
//            val intent = Intent(Intent.ACTION_VIEW,githubUrl)
//            startActivity(intent)
//        }
//    }
}