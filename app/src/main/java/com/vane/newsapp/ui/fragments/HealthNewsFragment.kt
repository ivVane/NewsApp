package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.vane.newsapp.R
import com.vane.newsapp.adapters.NewsLoadStateAdapter
import com.vane.newsapp.adapters.NewsPagingAdapter
import com.vane.newsapp.databinding.FragmentHealthNewsBinding
import com.vane.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthNewsFragment : Fragment(R.layout.fragment_health_news) {

    private val viewModel by viewModels<NewsViewModel>()

    private var _binding: FragmentHealthNewsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHealthNewsBinding.bind(view)

        val newsPagingAdapter = NewsPagingAdapter()

        binding.apply {
            recyclerViewHealthNews.setHasFixedSize(true)
            recyclerViewHealthNews.adapter = newsPagingAdapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { newsPagingAdapter.retry() },
                footer = NewsLoadStateAdapter { newsPagingAdapter.retry() }
            )
            // Setup our Load State for the paging.
            newsPagingAdapter.addLoadStateListener { loadState ->
                swipeRefreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
                // Only show the list if refresh succeeds.
                recyclerViewHealthNews.isVisible =
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

                // Reload trigger for the swipe layout
                swipeRefreshLayout.setOnRefreshListener {
                    newsPagingAdapter.refresh()
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }

        viewModel.getHealthNews.observe(viewLifecycleOwner) {
            newsPagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}