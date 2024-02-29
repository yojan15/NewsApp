package com.example.newsapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_articles")
data class CachedArticle(
    @PrimaryKey
    val url: String,
    val publishedAt: String,
    val urlToImage: String? = null,
    val description: String? = null,
    val title: String,
    val author: String? = null,
    val content: String? = null,
    val category: String
)

fun CachedArticle.toArticle(): Article {
    return Article(
        url = this.url,
        publishedAt = this.publishedAt,
        urlToImage = this.urlToImage,
        description = this.description,
        title = this.title,
        author = this.author,
        content = this.content,
        category = category
    )
}