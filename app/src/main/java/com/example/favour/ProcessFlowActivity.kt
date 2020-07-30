package com.example.favour

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class ProcessFlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val s: String = intent.extras!!.get("Request_Object").toString()
        val requestDTO = Gson().fromJson(s, RequestDTO::class.java)
        setContentView(R.layout.activity_process_flow)
        val bundle = Bundle()
        bundle.putString("RequestObject",s)
//        Log.i("Clicked Object",Gson().toJson(requestDTO))
        val frag = ViewRequestFragment()
        frag.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.containerProcess,frag).commit()
    }
}