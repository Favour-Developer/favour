package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val session = Session(this)
        if (session.getLoginState()!! && session.getSignUpState()!!) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
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