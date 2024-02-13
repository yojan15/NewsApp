package com.example.newsapp.api

import com.example.newsapp.data.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TechnologyNewsApi {
    @GET("top-headlines")
    fun getNews(
        @Query("source") source : String = "google-news",
        @Query("country") country: String = "in",
        @Query("category") category : String = "technology",
        @Query("apiKey") apiKey: String = "f8b7a3eefc6549d2822d921d51325364"
    ): Call<News>
}
