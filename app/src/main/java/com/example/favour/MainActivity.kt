package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.viewpager.widget.ViewPager
import com.example.favour.notifications.Token
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : NavigationDrawer() {
    private lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    val REQUEST_CODE = 200
    var isOpen: Boolean = false
    lateinit var faB_open: Animation
    lateinit var faB_close: Animation
    lateinit var faB_clock: Animation
    lateinit var faB_anti: Animation
//    lateinit var i: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateToken()

        acceptedRequests.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AcceptedRequest::class.java))
        })


        faB_open = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        faB_close = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        faB_clock = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_clock)
        faB_anti = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_anticlock)

        AddFavourRequest.setOnClickListener {
            if (isOpen) fabMenuClose()
            else fabMenuOpen()
        }

        shoppingButton.setOnClickListener(View.OnClickListener {
            startShopping()
        })
        borrowingButton.setOnClickListener(View.OnClickListener {
            startBorrowing()
        })
        shopping_tv.setOnClickListener(View.OnClickListener {
            startShopping()
        })
        borrowing_tv.setOnClickListener(View.OnClickListener {
            startBorrowing()
        })
        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.pager)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter: PagerAdapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

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

    private fun startBorrowing() {
        fabMenuClose()
        val i = Intent(this, AddFavour::class.java)
        i.putExtra("Type", 1)
        startActivity(i)
    }

    private fun startShopping() {
        fabMenuClose()
        val i = Intent(this, AddFavour::class.java)
        i.putExtra("Type", 0)
        startActivity(i)
    }

    private fun fabMenuOpen() {
        shadowView.visibility = View.VISIBLE
        shadowView.isClickable = true
        acceptedRequests.visibility = View.GONE
        borrowingButton.visibility = View.VISIBLE
        borrowing_tv.visibility = View.VISIBLE
        borrowingButton.startAnimation(faB_open)
        shoppingButton.visibility = View.VISIBLE
        shopping_tv.visibility = View.VISIBLE
        shoppingButton.startAnimation(faB_open)
        AddFavourRequest.startAnimation(faB_anti)
        borrowingButton.isClickable = true
        shoppingButton.isClickable = true
        isOpen = true
    }

    private fun fabMenuClose() {
        shadowView.visibility = View.INVISIBLE
        shadowView.isClickable = false
        acceptedRequests.visibility = View.VISIBLE
        shoppingButton.visibility = View.INVISIBLE
        shopping_tv.visibility = View.INVISIBLE
        shoppingButton.startAnimation(faB_close)
        borrowingButton.visibility = View.INVISIBLE
        borrowing_tv.visibility = View.INVISIBLE
        borrowingButton.startAnimation(faB_close)
        AddFavourRequest.startAnimation(faB_clock)
        borrowingButton.isClickable = false
        shoppingButton.isClickable = false
        isOpen = false
    }

    private fun updateToken() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token = Token(FirebaseInstanceId.getInstance().token!!)
        ref.child(firebaseUser!!.uid).setValue(token)
    }

}