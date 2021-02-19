package com.vane.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vane.newsapp.databinding.FragmentArticlePreviewBinding
import com.vane.newsapp.models.NewsArticle

class NewsPagingAdapter :
    PagingDataAdapter<NewsArticle, NewsPagingAdapter.NewsArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsArticleViewHolder {
        val binding = FragmentArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsArticleViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class NewsArticleViewHolder(private val binding: FragmentArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsArticle: NewsArticle) {
            binding.apply {
                Glide.with(itemView).load(newsArticle.urlToImage).into(imageViewArticleImg)
                textViewSource.text = newsArticle.source?.name
                textViewTitle.text = newsArticle.title
                textViewPublishAt.text = newsArticle.publishedAt
                textViewDescription.text = newsArticle.description

            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsArticle>() {
            override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle) =
                oldItem == newItem

        }
    }
}