package com.vane.newsapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vane.newsapp.api.NewsApi
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.utils.Constants
import retrofit2.HttpException
import java.io.IOException


private const val SEARCH_NEWS_STARTING_PAGE_INDEX = 1

class SearchNewsPagingSource(
    val newsApi: NewsApi,
    val searchQuery: String,
    val language: String
) : PagingSource<Int, NewsArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val position = params.key ?: SEARCH_NEWS_STARTING_PAGE_INDEX

        return try {
            val response = newsApi.searchForNews(
                searchQuery,
                language,
                Constants.API_KEY,
                position,
                params.loadSize
            )
            val articles = response.articles
            LoadResult.Page(
                data = articles,
                prevKey = if (position == SEARCH_NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (articles.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}