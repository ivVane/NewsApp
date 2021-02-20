package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.vane.newsapp.R
import com.vane.newsapp.adapters.NewsLoadStateAdapter
import com.vane.newsapp.adapters.NewsPagingAdapter
import com.vane.newsapp.databinding.FragmentSearchNewsBinding
import com.vane.newsapp.models.NewsArticle
import com.vane.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news),
    NewsPagingAdapter.OnItemClickListener {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchNewsBinding.bind(view)

        val newsPagingAdapter = NewsPagingAdapter(this)

        binding.apply {
            recyclerViewSearchNews.setHasFixedSize(true)
            recyclerViewSearchNews.adapter = newsPagingAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { newsPagingAdapter.retry() },
                footer = NewsLoadStateAdapter { newsPagingAdapter.retry() }
            )
            newsPagingAdapter.addLoadStateListener { loadState ->
                // Only show the list if refresh succeeds.
                recyclerViewSearchNews.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        context,
                        "\\uD83D\\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                // Our retry button should trigger a reload of the PagingData.
                buttonRetry.setOnClickListener { newsPagingAdapter.retry() }
            }

            viewModel.searchArticle.observe(viewLifecycleOwner) {
                newsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }

            setHasOptionsMenu(true)
        }
    }

    // Creating menu for our SearchView
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.search_news_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerViewSearchNews.scrollToPosition(0)
                    viewModel.searchForArticles(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    // Navigate to ArticleFragment
    override fun onItemClick(newsArticle: NewsArticle) {
        val action =
            SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(newsArticle)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}