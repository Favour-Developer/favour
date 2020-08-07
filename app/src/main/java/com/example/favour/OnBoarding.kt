package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OnBoarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        supportFragmentManager.beginTransaction()
            .replace(R.id.onboarding_container, FragmentOnboarding1())
            .addToBackStack("FragOnboard1").commit()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount == 1) finish()
        else super.onBackPressed()
    }
}