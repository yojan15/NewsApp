package com.example.newsapp.api

import com.example.newsapp.data.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    fun getNews(
        @Query("source") source : String = "google-news",
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = "ed2aa9a39cc344459a5f147eb22f6433"
    ): Call<News>

    @GET("everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "ed2aa9a39cc344459a5f147eb22f6433"
    ): Call<News>
}
