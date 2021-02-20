package com.vane.newsapp.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    // Handling Search for news articles.
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val searchArticle: LiveData<PagingData<NewsArticle>> = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchForArticles(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "default_query"
    }

    // Function to save(insert) our favorite Articles into our Room Database(article_db).
    fun saveArticle(article: NewsArticle) = viewModelScope.launch {
        repository.upsert(article)
    }

    // Function to delete an Articles from our Room Database(article_db).
    fun deleteArticle(article: NewsArticle) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getSavedNews() = repository.getSavedNews()
}