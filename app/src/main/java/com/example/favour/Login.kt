package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
//        val progressDialog: ProgressBar? = null
        mAuth = FirebaseAuth.getInstance()
        //action on clicking login button
        login.setOnClickListener {
            if (!IsOnline().connectedToInternet(applicationContext)) {
                showDialog()
            }


            //checking the empty fields
            if (CheckerMatcher().checkEmptyEmailPass(emailtext, passText)) {
//                progressDialog.V
                mAuth!!.signInWithEmailAndPassword(
                    emailtext.text.toString(),
                    passText.text.toString()
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
//                            val user = mAuth!!.currentUser
                            val data = getSharedPreferences("Data", Context.MODE_PRIVATE)
                            data.edit().putBoolean("isLogged", true)
                                .apply()

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
//
//    private fun checkEmptyField(emailText: EditText, pasText: EditText): Boolean {
//        if (!isEmail(emailtext)) {
//            emailtext.error = "Enter valid email."
//            return false
//        }
//        if (!isPassword(passText.text.toString())) {
//            passText.error = "Enter valid password."
//            return false
//        }
//        return true
//    }
//
//    //to check whether the string entered in the email field is in the format of email address
//    private fun isEmail(text: EditText): Boolean {
//        val email: CharSequence = text.text.toString()
//        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email)
//            .matches()
//    }
//
//    //to check whether the length of the password is atleast of 8 characters
//    private fun isPassword(password: String): Boolean {
//        return !TextUtils.isEmpty(password) && password.length >= 8
//    }

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