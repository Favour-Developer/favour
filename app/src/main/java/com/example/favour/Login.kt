package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val session = Session(this)
//        val progressDialog: ProgressBar? = null
        mAuth = FirebaseAuth.getInstance()
//        val progressBar = ProgressBar(this)
        //action on clicking login button
        login.setOnClickListener {
            if (!IsOnline().connectedToInternet(applicationContext)) {
                showDialog()
            }


            //checking the empty fields
            if (CheckerMatcher().checkEmptyPhonePass(mobileLogin, passLogin)) {
//                progressDialog.V
                mAuth!!.signInWithEmailAndPassword(
                    mobileLogin.text.toString() + "@favour.com" ,
                    passLogin.text.toString()
                )
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithEmail:success")
                            Toast.makeText(
                                this, "Authentication Success.",
                                Toast.LENGTH_SHORT
                            ).show()

                            session.setLoginState(true)
                            session.setMobile(mobileLogin.text.toString())
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(
                                "Failed",
                                "signInWithEmail:failure",
                                task.exception
                            )
                            Toast.makeText(
                                this, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        // ...
                    }
            }
        }

    }

    //dialog box to show no internet connection
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("No Internet Connection")
            .setPositiveButton(
                "Retry"
            ) { dialogInterface, _ ->
                if (!IsOnline().connectedToInternet(applicationContext)) {
                    dialogInterface.dismiss()
                } else showDialog()
            }
            .create()
            .show()
    }
}