package com.example.favour

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class CompletedFavourPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val title = arrayOf("Asked",  "Responded")

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun getItem(position: Int): Fragment {
        if(position == 0)return CompletedAskedFragment()
        return CompletedRespondedFragment()

    }

    override fun getCount(): Int {
        return 2
    }
}