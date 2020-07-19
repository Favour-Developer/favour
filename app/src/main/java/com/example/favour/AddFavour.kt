package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_favour_request.*

class AddFavour: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_favour_request)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        PlaceFavourRequest.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        textView4.setOnClickListener {
            startActivity(Intent(this,col))
        }
    }
}