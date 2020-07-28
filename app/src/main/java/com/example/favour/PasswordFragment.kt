package com.example.favour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val session = Session(requireContext())
        val s1 =  "Password for Mobile Number" + " XXXXXX" + (session.getMobile()?.takeLast(4))
        passMobText.text = s1
        Log.d("Username",session.getUsername().toString())
        Log.d("Mobile",session.getMobile().toString())
        Log.d("Verified", session.getVerifiedState().toString())
        passSigUp.setOnClickListener(View.OnClickListener {

            if (CheckerMatcher().checkPassPass(pass1, pass2)) {

                startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }
        })
    }

}