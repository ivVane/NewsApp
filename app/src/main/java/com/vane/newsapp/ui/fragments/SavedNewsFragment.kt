package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vane.newsapp.R
import com.vane.newsapp.adapters.NewsAdapter
import com.vane.newsapp.databinding.FragmentSavedNewsBinding
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedNewsFragment : Fragment(R.layout.fragment_saved_news),
    NewsAdapter.OnItemClickListener {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedNewsBinding.bind(view)

        val newsAdapter = NewsAdapter(this)

        binding.apply {
            recyclerViewSavedNews.setHasFixedSize(true)
            recyclerViewSavedNews.adapter = newsAdapter
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            // OnSwipe Right or Left delete an Article from our Room database.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val newsArticle = newsAdapter.differList.currentList[position]

                viewModel.deleteArticle(newsArticle)

                Snackbar.make(
                    view,
                    getString(R.string.deleted_article_label),
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(newsArticle)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerViewSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner) {
            newsAdapter.differList.submitList(it)
        }
    }

    // Navigate to ArticleFragment
    override fun onItemClick(newsArticle: NewsArticle) {
        val action =
            SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(newsArticle)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}