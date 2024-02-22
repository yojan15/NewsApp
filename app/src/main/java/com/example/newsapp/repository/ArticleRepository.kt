package com.example.newsapp.repository

import android.app.appsearch.StorageInfo
import androidx.lifecycle.LiveData
import com.example.newsapp.data.Article
import com.example.newsapp.data.ArticleDao
import com.example.newsapp.data.CachedArticle

class ArticleRepository(private val articleDao : ArticleDao) {
    val allArticle : LiveData<List<Article>> = articleDao.getAllArticle()

    val allCacheArticles : LiveData<List<CachedArticle>> = articleDao.getAllCachedArticles()

    suspend fun insert(article: Article) {
        val cachedArticle = CachedArticle(
            url = article.url,
            publishedAt = article.publishedAt,
            urlToImage = article.urlToImage,
            description = article.description,
            title = article.title,
            author = article.author,
            content = article.content
        )
        articleDao.insertCachedArticle(cachedArticle)
    }
    suspend fun delete(article: Article) {
        articleDao.delete(article)
        articleDao.getCachedArticle(article.url)?.let {
            articleDao.deleteCachedArticle(article)
        }
    }

    fun isArticleSaved(url:String) : LiveData<Boolean> {
        return articleDao.isArticleSaved(url)
    }
}