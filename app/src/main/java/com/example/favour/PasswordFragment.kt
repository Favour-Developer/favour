package com.example.favour

import android.content.ContentValues.TAG
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_front_signin.*
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mobileNum = arguments?.getString("Mobile").toString()
        name = arguments?.getString("Name").toString()
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    companion object {
        lateinit var mobileNum: String
        lateinit var name: String
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val session = Session(requireContext())
        val auth = FirebaseAuth.getInstance()
        val s1 = "Password for Mobile Number" + " XXXXXX" + (mobileNum.takeLast(4))
        passMobText.text = s1
        Log.d("Username", session.getUsername().toString())
        Log.d("Mobile", session.getMobile().toString())
        Log.d("Verified", session.getVerifiedState().toString())
        passSigUp.setOnClickListener(View.OnClickListener {
            if (CheckerMatcher().checkPassPass(pass1, pass2)) {
                val emailGenerate = "$mobileNum@favour.com"
                auth.createUserWithEmailAndPassword(emailGenerate, pass2.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
//                                val user = auth.currentUser
                            session.setVerifiedState(true)
                            session.setLoginState(true)
                            session.setSignUpState(true)
                            session.setMobile(mobileNum)
                            session.setUsername(name)
                            startActivity(Intent(context, MainActivity::class.java))
                            activity?.finish()
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                requireContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        })
    }

}