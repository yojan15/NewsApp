package com.example.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {
    private const val BASE_URL = "https://newsapi.org/v2/"
    
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsApi: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }
    val businessNewsApi : BusinessNewsApi by lazy {
        retrofit.create(BusinessNewsApi::class.java)
    }
    val entertainmentNewApi : EntertainmentNewApi by lazy {
        retrofit.create(EntertainmentNewApi::class.java)
    }
    val healthNewsApi : HealthNewsApi by lazy {
        retrofit.create(HealthNewsApi::class.java)
    }
    val scienceNewsApi : ScienceNewsApi by lazy {
        retrofit.create(ScienceNewsApi::class.java)
    }
    val sportsNewsApi : SportsNewsApi by lazy {
        retrofit.create(SportsNewsApi::class.java)
    }
    val technologyNewsApi : TechnologyNewsApi by lazy {
        retrofit.create(TechnologyNewsApi::class.java)
    }
}
