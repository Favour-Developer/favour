package com.example.favour

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*


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
                            session.databaseRoot().child(session.USERS)
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
                                                session.setAddress(userDTO.address)
                                                val ref =
                                                    session.storageRoot().child("Profile_photos")
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