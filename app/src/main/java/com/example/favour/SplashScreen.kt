package com.example.favour

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed

class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed(Runnable {
            if (getSharedPreferences("Data", Context.MODE_PRIVATE).getBoolean("isLogged", false)) {
                startActivity(Intent(this, MainActivity::class.java))
            } else startActivity(Intent(this, Login::class.java))
            finish()
        }, SPLASH_TIME_OUT.toLong())

    }
}