package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_favour.*
import kotlinx.android.synthetic.main.view_user_request.*
import kotlinx.android.synthetic.main.view_user_request.BackButtonToHome

class ViewFragment: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_user_request)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        AcceptRequest.setOnClickListener {
            startActivity(Intent(this, AcceptRequestFragment::class.java))
        }
    }
}
