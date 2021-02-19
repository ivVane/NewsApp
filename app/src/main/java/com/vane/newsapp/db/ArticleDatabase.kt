package com.vane.newsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.utils.Converters

@Database(
    entities = [NewsArticle::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): NewsArticleDao

}