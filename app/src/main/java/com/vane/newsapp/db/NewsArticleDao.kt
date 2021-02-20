package com.vane.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vane.newsapp.models.NewsArticle

@Dao
interface NewsArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: NewsArticle): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<NewsArticle>>

    @Delete
    suspend fun deleteArticle(article: NewsArticle)

}