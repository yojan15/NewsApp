package com.example.newsapp.data

data class News (
    val totalResults: Long,
    val articles: List<Article>,
    val status: String
)

data class Article (
    val publishedAt: String,
    val author: String,
    val source: Source,
    val title: String,
    val url: String
)

data class Source (
    val name: Name,
    val id: ID
)

enum class ID {
    GoogleNewsIn
}

enum class Name {
    GoogleNewsIndia
}
