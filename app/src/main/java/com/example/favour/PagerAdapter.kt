package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
//            val favourRequests = RequestFragment()
//            val args = Bundle()
//            args.putString("Favour Requests", "favourRequests")
//            favourRequests.arguments = args
            return MyRequestFragment()
        }
//        val myRequests = RequestFragment()
//        val args = Bundle()
//        args.putString("Favour Requests", "myRequests")
//        myRequests.arguments = args
        return RequestFragment()

    }

    override fun getCount(): Int {
        return 2
    }
}