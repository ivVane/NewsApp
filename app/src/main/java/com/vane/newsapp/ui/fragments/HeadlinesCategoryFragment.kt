package com.vane.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.vane.newsapp.R
import com.vane.newsapp.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_headlines_category.*

class HeadlinesCategoryFragment : Fragment(R.layout.fragment_headlines_category) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(BreakingNewsFragment(), "Top Headlines")
        adapter.addFragment(SportsNewsFragment(), "Sports")
        adapter.addFragment(HealthNewsFragment(), "Health")
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
    }
}