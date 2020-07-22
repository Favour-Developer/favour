package com.example.favour

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPermissions()
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
// CAMERA SETUP
// https://pranaybhalerao.wordpress.com/2018/02/11/run-time-permission-in-androidkotlin/

    private val CAMERA_REQUEST_CODE=123;
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission!=PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }
    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}