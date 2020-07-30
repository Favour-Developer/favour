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
        val session = Session(this)
        Handler().postDelayed(Runnable {
            if (session.getLoginState()!! && session.getSignUpState()!!) {
                startActivity(Intent(this, MainActivity::class.java))
            } else if
            (!session.getLoginState()!! && !session.getSignUpState()!!) startActivity(Intent(this, SignUp::class.java))
            else
                startActivity(Intent(this, Login::class.java))
            finish()
        }, SPLASH_TIME_OUT.toLong())

    }
}