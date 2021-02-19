package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.vane.newsapp.R
import com.vane.newsapp.databinding.FragmentArticleBinding
import com.vane.newsapp.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val viewModel by viewModels<NewsViewModel>()

    private val args by navArgs<ArticleFragmentArgs>()

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentArticleBinding.bind(view)

        binding.apply {
            val newsArticle = args.newsArticle
            webViewArticle.apply {
                webViewClient = WebViewClient()
                loadUrl(newsArticle.url)
            }
            fab.setOnClickListener {
                // We will implement this later.
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}