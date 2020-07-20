package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AddFavourRequest.setOnClickListener {
            startActivity(Intent(this, AddFavour::class.java))
        }
            val tabLayout: TabLayout = findViewById(R.id.tabs)
            val viewPager: ViewPager = findViewById(R.id.pager)

//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

            tabLayout.addTab(tabLayout.newTab().setText("Favour Requests"))
            tabLayout.addTab(tabLayout.newTab().setText("My Requests"))
            tabLayout.tabGravity = TabLayout.GRAVITY_FILL

            val adapter = PagerAdapter(supportFragmentManager)
            viewPager.adapter = adapter
//        tabLayout.setupWithViewPager(viewPager)


            viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))


            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    viewPager.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                }

            })
        }

    }