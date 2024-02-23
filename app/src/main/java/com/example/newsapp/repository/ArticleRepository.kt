package com.example.newsapp.repository

import android.app.appsearch.StorageInfo
import androidx.lifecycle.LiveData
import com.example.newsapp.data.Article
import com.example.newsapp.data.ArticleDao
import com.example.newsapp.data.CachedArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository(private val articleDao: ArticleDao) {

    val allArticle: LiveData<List<Article>> = articleDao.getAllArticle()

    val allCacheArticles: LiveData<List<CachedArticle>> = articleDao.getAllCachedArticles()


    suspend fun saveArticle(article: Article) {
        articleDao.insert(article)
    }

    suspend fun insert(article: Article) {
        val cachedArticle = article.toCachedArticle()
        articleDao.insertCachedArticle(cachedArticle)
    }
    suspend fun deleteAllCachedArticles() {
        withContext(Dispatchers.IO) {
            articleDao.deleteCachedArticle()
        }
    }
    fun isArticleSaved(url: String): LiveData<Boolean> {
        return articleDao.isArticleSaved(url)
    }

    private fun Article.toCachedArticle(): CachedArticle {
        return CachedArticle(
            url = this.url,
            publishedAt = this.publishedAt,
            urlToImage = this.urlToImage,
            description = this.description,
            title = this.title,
            author = this.author,
            content = this.content
        )
    }
    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}
