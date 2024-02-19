package com.example.newsapp.repository

import android.app.appsearch.StorageInfo
import androidx.lifecycle.LiveData
import com.example.newsapp.data.Article
import com.example.newsapp.data.ArticleDao

class ArticleRepository(private val articleDao : ArticleDao) {
    val allArticle : LiveData<List<Article>> = articleDao.getAllArticle()

    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }
    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }

    fun isArticleSaved(url:String) : LiveData<Boolean> {
        return articleDao.isArticleSaved(url)
    }
}