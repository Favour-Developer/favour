package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_on_boarding.*


class OnBoarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        val viewPager = findViewById<View>(R.id.view_pager) as ViewPager
        val adapter = PagerOnboardingAdapter(this)
        viewPager.adapter = adapter
        viewPager.currentItem = 0

        finishOnboarding.setOnClickListener(View.OnClickListener {
            Session(this).setFirstLaunch(false)
            startActivity(Intent(this,SignUp::class.java))
            finish()
        })

    }
}