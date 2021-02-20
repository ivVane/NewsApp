package com.vane.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vane.newsapp.databinding.FragmentArticlePreviewBinding
import com.vane.newsapp.models.NewsArticle

/**
 * We created this adapter to use it for our SavedNewsFragment to display the articles
 * saved into our local Room database.
 */
class NewsAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NewsAdapter.SavedNewsViewHolder>() {

    val differList = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedNewsViewHolder {
        val binding = FragmentArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SavedNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedNewsViewHolder, position: Int) {
        val currentItem = differList.currentList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int = differList.currentList.size

    inner class SavedNewsViewHolder(private val binding: FragmentArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = differList.currentList[absoluteAdapterPosition]
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(newsArticle: NewsArticle) {
            binding.apply {
                // val newsArticle = differList.currentList[absoluteAdapterPosition]
                Glide.with(itemView).load(newsArticle.urlToImage).into(imageViewArticleImg)
                textViewSource.text = newsArticle.source?.name
                textViewTitle.text = newsArticle.title
                textViewPublishAt.text = newsArticle.publishedAt
                textViewDescription.text = newsArticle.description
            }
        }
    }

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