package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.data.Article
import com.example.newsapp.data.ArticleDao
import com.example.newsapp.data.CachedArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticleRepository(private val articleDao: ArticleDao) {

    /**
     * get all the articles from Article table
     */
    val allArticle: LiveData<List<Article>> = articleDao.getAllArticle()

    /**
     * get all the articles from CacheArticle table
     */
    val allCacheArticles: LiveData<List<CachedArticle>> = articleDao.getAllCachedArticles()

    suspend fun saveArticle(article: Article) {
        /**
         * save the Article in Article database manually when user clicks
         * on title
         */
        articleDao.insert(article)
    }
    suspend fun deleteAllCachedArticles() {
        /**
         * delete cached article
         * for example when user when user is accessing application over the internet
         * it will save the articles in cacheArticle table and when user try to access application
         * without internet it will fetch the cachedArticles , however when user gets back on internet
         * it will delete all the previously saved articles
         */
        withContext(Dispatchers.IO) {
            articleDao.deleteCachedArticle()
        }
    }
    fun isArticleSaved(url: String): LiveData<Boolean> {
        return articleDao.isArticleSaved(url)
    }

    suspend fun insertByCategory(article: Article, category: String): CachedArticle {
        val cachedArticle = article.toCachedArticle(category)
        articleDao.insertCachedArticle(cachedArticle)
        return cachedArticle
    }

    fun getArticlesByCategory(category: String): LiveData<List<CachedArticle>> {
        return articleDao.getArticlesByCategory(category)
    }


    private fun Article.toCachedArticle(category: String): CachedArticle {
        return CachedArticle(
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
    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}
