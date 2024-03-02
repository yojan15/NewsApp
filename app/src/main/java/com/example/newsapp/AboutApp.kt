package com.example.newsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.newsapp.databinding.ActivityAboutAppBinding

class AboutApp : AppCompatActivity() {
    private lateinit var binding : ActivityAboutAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        githubLink()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun githubLink() {
        binding.github.setOnClickListener {
            val githubUrl = Uri.parse("https://github.com/yojan15/NewsApp")
            val intent = Intent(Intent.ACTION_VIEW,githubUrl)
            startActivity(intent)
        }
    }
}