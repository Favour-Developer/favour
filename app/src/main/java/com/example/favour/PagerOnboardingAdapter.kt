package com.example.favour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager


class PagerOnboardingAdapter : PagerAdapter {

    private val images =
        intArrayOf(R.drawable.guide_page_1, R.drawable.guide_page_2, R.drawable.guide_page_3)
    lateinit var context: Context

    var mLayoutInflater: LayoutInflater? = null

    constructor(){}


    constructor(context: Context) : this() {
        this.context = context
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            mLayoutInflater!!.inflate(R.layout.fragment_onboarding, container, false)
        val imageView: ImageView =
            itemView.findViewById<View>(R.id.guideImage) as ImageView
        imageView.setImageResource(images[position])
        container.addView(itemView,0)
        return itemView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}