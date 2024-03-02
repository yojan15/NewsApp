package com.example.newsapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM article")
    fun getAllArticle(): LiveData<List<Article>>

    @Query("SELECT * FROM cached_articles")
    fun getAllCachedArticles(): LiveData<List<CachedArticle>>

    @Query("SELECT EXISTS (SELECT 1 FROM ARTICLE WHERE url = :url)")
    fun isArticleSaved(url: String): LiveData<Boolean>

    @Query("SELECT * FROM cached_articles WHERE url = :url")
    fun getCachedArticle(url: String): LiveData<CachedArticle?>

    @Query("delete from cached_articles")
    fun deleteCachedArticle()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedArticle(cachedArticle: CachedArticle)

    @Query("SELECT * FROM cached_articles WHERE category = :category")
    fun getArticlesByCategory(category: String): LiveData<List<CachedArticle>>
}
