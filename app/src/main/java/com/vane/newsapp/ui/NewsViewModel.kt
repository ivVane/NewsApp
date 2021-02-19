package com.vane.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    // Breaking news articles.
    val getBreakingNews: LiveData<PagingData<NewsArticle>> = repository.getBreakingNews()
        .cachedIn(viewModelScope)

    // Sports category news articles.
    val getSportsNews: LiveData<PagingData<NewsArticle>> = repository.getSportsNews()
        .cachedIn(viewModelScope)

    // Health category news articles.
    val getHealthNews: LiveData<PagingData<NewsArticle>> = repository.getHealthNews()
        .cachedIn(viewModelScope)
}