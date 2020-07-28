package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.fragment_otp.*


class OtpFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val session = Session(requireContext())
        session.setVerifiedState(true)
        val s1 = "One Time Password (OTP) has been sent to your mobile"
        val s2 = ", please enter the same here to login"
        val s = s1 + " XXXXXX" + (session.getMobile()?.takeLast(4)) + s2
        otpText.text = s
        otpSignUp.setOnClickListener(View.OnClickListener {

//            val fml = R.id.fml_signin
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fml_signin, PasswordFragment()).addToBackStack("FragOTP")
                .commit()


        })
    }

}