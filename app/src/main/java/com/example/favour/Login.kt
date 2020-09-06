package com.example.favour

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_front_signin.*


@Suppress("DEPRECATION")
class Login : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val session = Session(this)
        takeMeSignUp.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        })
        mAuth = FirebaseAuth.getInstance()

        login.setOnClickListener {
            if (!IsOnline().connectedToInternet(applicationContext)) {
                showDialog()
                return@setOnClickListener
            }


            //checking the empty fields
            if (CheckerMatcher().checkEmptyPhonePass(mobileLogin, passLogin)) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Logging in ...")
                progressDialog.show()

                mAuth!!.signInWithEmailAndPassword(
                    mobileLogin.text.toString() + "@favour.com",
                    passLogin.text.toString()
                )
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {
                            session.setLoginState(true)
                            session.setSignUpState(true)
                            FirebaseDatabase.getInstance().reference.child(session.USERS)
                                .child(FirebaseAuth.getInstance().uid!!)
                                .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val userDTO = snapshot.getValue(UserDTO::class.java)
                                            if (userDTO != null) {
                                                session.setMobile(userDTO.mobile)
                                                session.setUsername(userDTO.username)
                                                session.setEmail(userDTO.email)
                                                session.setGender(userDTO.gender)
                                                val ref =
                                                    FirebaseStorage.getInstance().reference.child("Profile_photos")
                                                        .child(FirebaseAuth.getInstance().uid!!)
                                                ref.downloadUrl.addOnSuccessListener { uri ->
                                                    session.setPhotoUrl(uri.toString())
                                                }
                                                progressDialog.dismiss()
                                                startActivity(
                                                    Intent(
                                                        applicationContext,
                                                        MainActivity::class.java
                                                    )
                                                )
                                                finish()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })


                        } else {
                            progressDialog.dismiss()
                            Snackbar.make(
                                rootLogin, "Authentication failed.",
                                Snackbar.LENGTH_SHORT
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