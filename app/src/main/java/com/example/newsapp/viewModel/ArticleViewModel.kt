package com.example.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.AppDatabase
import com.example.newsapp.data.Article
import com.example.newsapp.repository.ArticleRepository
import kotlinx.coroutines.launch

class ArticleViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: ArticleRepository
    val allArticle: LiveData<List<Article>>

    init {
        val articleDao = AppDatabase.getDatabase(application).articleDao()
        repository = ArticleRepository(articleDao)
        allArticle = repository.allArticle
    }

    fun insert(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun delete(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun isArticleSaved(url: String): LiveData<Boolean> {
        return repository.isArticleSaved(url)
    }
}