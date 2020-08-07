package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val session = Session(this)
        if (session.getLoginState()!! && session.getSignUpState()!!)
            startActivity(Intent(this, MainActivity::class.java))
//        val fml = findViewById<FrameLayout>(R.id.fml_signin)
        supportFragmentManager.beginTransaction().add(R.id.fml_signin, FrontSigninFragment())
            .addToBackStack("FragSignInFront").commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            super.onBackPressed()
        else finish()
    }
}