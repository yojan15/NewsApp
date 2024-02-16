package com.example.newsapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class News (
    val totalResults: Long,
    val articles: List<Article>,
    val status: String
)
@Parcelize

data class Article(
    val publishedAt: String,
    val urlToImage: String? = null,
    val description: String? = null,
//    val source: Source,
    val title: String,
    val url: String,
    val author: String? = null,
    val content: String? = null
) : Parcelable

data class Source (
    val name: String,
    val id: String? = null
)