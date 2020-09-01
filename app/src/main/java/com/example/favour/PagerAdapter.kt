package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val title = arrayOf("Favour Requests", "My Requests")

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun getItem(position: Int): Fragment {
        if (position == 0) return RequestFragment()
        return MyRequestFragment()

    }

    override fun getCount(): Int {
        return 2
    }
}