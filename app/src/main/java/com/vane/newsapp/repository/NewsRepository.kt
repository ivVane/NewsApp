package com.vane.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.vane.newsapp.api.NewsApi
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.utils.Constants.DEFAULT_PAGE_SIZE
import com.vane.newsapp.utils.Constants.MAX_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi
) {

    // Query for Breaking News headlines.
    fun getBreakingNews(): LiveData<PagingData<NewsArticle>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsApi, "", "us")
            }
        ).liveData

    // Query for Sports News headlines.
    fun getSportsNews(): LiveData<PagingData<NewsArticle>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsApi, "sports", "")
            }
        ).liveData

    // Query for Health News headlines.
    fun getHealthNews(): LiveData<PagingData<NewsArticle>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(newsApi, "health", "")
            }
        ).liveData
}