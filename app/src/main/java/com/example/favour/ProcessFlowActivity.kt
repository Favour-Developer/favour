package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class ProcessFlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val s: String = intent.extras!!.get("Request_Object").toString()
        val requestDTO = Gson().fromJson(s, RequestDTO::class.java)
        setContentView(R.layout.activity_process_flow)
        val bundle = Bundle()
        bundle.putString("RequestObject", s)
//        Log.i("Clicked Object",Gson().toJson(requestDTO))
        val frag: Fragment

        frag = if (requestDTO.isProgress && !requestDTO.isCompleted) ProcessRequestFragment()
        else ViewRequestFragment()
        frag.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.containerProcess, frag).commit()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}