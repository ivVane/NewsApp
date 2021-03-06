package com.vane.newsapp.api

import com.vane.newsapp.models.NewsResponse
import com.vane.newsapp.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String,
        @Query("category")
        category: String,
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("page")
        pageNumber: Int,
        @Query("pageSize")
        pageSize: Int
    ): NewsResponse


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("language")
        language: String,
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("page")
        pageNumber: Int,
        @Query("pageSize")
        pageSize: Int
    ): NewsResponse

}