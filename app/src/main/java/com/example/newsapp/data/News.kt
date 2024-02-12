package com.example.newsapp.data

data class News (
    val totalResults: Long,
    val articles: List<Article>,
    val status: String
)

data class Article (
    val publishedAt: String,
    val urlToImage: String? = null,
    val description: String? = null,
    val source: Source,
    val title: String,
    val url: String,
    val author: String? = null,
    val content: String? = null
)

data class Source (
    val name: String,
    val id: String? = null
)
