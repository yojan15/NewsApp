package com.example.newsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsapp.fragments.Business
import com.example.newsapp.fragments.TopHeadlines

class MyViewPagerAdapter(fragment : FragmentActivity) :FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> TopHeadlines()
            1-> Business()

            else -> TopHeadlines()
        }
    }
}