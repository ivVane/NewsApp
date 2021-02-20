package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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