package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : NavigationDrawer() {
    private lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    val REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AddFavourRequest.setOnClickListener {
            startActivityForResult(Intent(this, AddFavour::class.java),REQUEST_CODE)
        }
        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.pager)

//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter: PagerAdapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
//        tabLayout.setupWithViewPager(viewPager)
//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                viewPager.currentItem = tab!!.position
//            }

//            viewPager.adapter = adapter
            tabLayout.setupWithViewPager(viewPager)


//            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


            tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                }

            })
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
    }

    }