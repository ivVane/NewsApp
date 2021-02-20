package com.vane.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vane.newsapp.databinding.FragmentArticlePreviewBinding
import com.vane.newsapp.models.NewsArticle

class NewsPagingAdapter(private val listener: OnItemClickListener) :
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

        // Initialize an OnCLickListener for this RecyclerView Adapter. We want to use
        // the OnClickListener on the whole card that's why we use "binding.root.setOnClickListener".
        // We want to send this click to the underlain fragment that contains the RecyclerView,
        // we don't want to handle it right here because we can't handle navigation from inside a fragment.
        // For this we need to create an Interface
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

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

    // Our fragment later will implement this interface, and we can pass the fragment it self over to the
    // constructor to the adapter and call this method on it. We can also pass the fragment with out this interface
    // but in that case the adapter will be tightly coupled with the fragment, and with such interface we are keeping
    // the adapter reusable (we can easily use the adapter with another fragment later if we want).
    interface OnItemClickListener {
        fun onItemClick(newsArticle: NewsArticle)
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