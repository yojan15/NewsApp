package com.example.newsapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.AppDatabase
import com.example.newsapp.data.Article
import com.example.newsapp.data.CachedArticle
import com.example.newsapp.repository.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    /**
     *  Private repository to encapsulate data access
     */
    private val repository: ArticleRepository

    /**
     * LiveData for observing the list of saved articles
     */
    private val _allArticle: LiveData<List<Article>>


    /**
     *  LiveData for observing the list of cached articles
     */
    private val _allCachedArticles: LiveData<List<CachedArticle>>

    /**
     * public access live data for save article
     */
    val allArticle: LiveData<List<Article>> get() = _allArticle

    /**
     * public access live data for cache article
     */
    val allCachedArticles: LiveData<List<CachedArticle>> get() = _allCachedArticles

    init {
        val articleDao = AppDatabase.getDatabase(application).articleDao()
        repository = ArticleRepository(articleDao)
        _allArticle = repository.allArticle
        _allCachedArticles = repository.allCacheArticles
    }

    /**
     * Insert an article into the database.
     * @param article The article to be inserted.
     */


    fun delete(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    /**
     * Delete an article from the database.
     * @param article The article to be deleted.
     */
    fun deleteAllCachedArticles() = viewModelScope.launch {
        repository.deleteAllCachedArticles()
    }
    /**
     * Check if an article is saved.
     * @param url The URL of the article.
     * @return LiveData indicating whether the article is saved.
     */
    fun isArticleSaved(url: String): LiveData<Boolean> {
        return repository.isArticleSaved(url)
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.saveArticle(article)
    }

    fun insertByCategory(article: Article, category: String) = viewModelScope.launch {
        repository.insertByCategory(article, category)
    }

    fun getArticlesByCategory(category: String): LiveData<List<CachedArticle>> {
        return repository.getArticlesByCategory(category)
    }
}
