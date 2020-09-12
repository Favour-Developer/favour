package com.example.favour

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = Session(requireContext())
        mobileNum = session.getMobile()!!
        name = session.getUsername()!!
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    companion object {
        lateinit var mobileNum: String
        lateinit var name: String
        lateinit var session: Session
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val s1 = "Password for Mobile Number" + " XXXXXX" + (mobileNum.takeLast(4))
        passMobText.text = s1
        Log.d("Username", session.getUsername().toString())
        Log.d("Mobile", session.getMobile().toString())
        Log.d("Verified", session.getVerifiedState().toString())
        passSigUp.setOnClickListener(View.OnClickListener {
            if (CheckerMatcher().checkPassPass(pass1, pass2)) {
                val progressDialog = ProgressDialog(requireContext())
                progressDialog.setMessage("Signing up ...")
                progressDialog.show()
                val emailGenerate = "$mobileNum@favour.com"
                val emailCredential = EmailAuthProvider.getCredential(emailGenerate, pass2.text.toString())
                completeSignUp(emailCredential)
            }


        })
    }

    private fun completeSignUp(emailCredential: AuthCredential) {
        FirebaseAuth.getInstance().currentUser?.linkWithCredential(emailCredential)
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    session.setVerifiedState(true)
                    session.setLoginState(true)
                    session.setSignUpState(true)
                    session.setMobile(mobileNum)
                    session.setUsername(name)
                    session.databaseRoot().child("Users")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .setValue(
                            UserDTO(
                                session.getUsername().toString(),
                                "",
                                "",
                                "",
                                "",
                                session.getMobile().toString()
                            )
                        )
                    startActivity(Intent(context, MainActivity::class.java))
                    activity?.finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Snackbar.make(
                            rootSignUp, "SignUp failed. Maybe account already exists!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

}