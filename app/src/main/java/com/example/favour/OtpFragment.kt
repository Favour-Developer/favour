package com.example.favour

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_otp.*
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class OtpFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = Session(requireContext())
        mobileNum = session.getMobile()
        name = session.getUsername()
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    companion object {
        var mobileNum: String? = null
        var name: String? = null
        var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
        lateinit var session: Session
        var verificationId: String? = null
        var code: PhoneAuthProvider.ForceResendingToken? = null

    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val timer = object : CountDownTimer(120000, 1000) {
            override fun onFinish() {
                OTPtimer.visibility = View.GONE
                otpResend.setTextColor(resources.getColor(R.color.blue))
            }

            override fun onTick(tick: Long) {
                val s: String = resources.getString(R.string.otp_timer) + (tick / 1000).toString()
                OTPtimer.text = s
            }
        }
        timer.start()
        otpResend.setOnClickListener(View.OnClickListener {
            if (otpResend.textColors.defaultColor == resources.getColor(R.color.blue)) {
                otpResend.setTextColor(resources.getColor(R.color.grey))
                OTPtimer.visibility = View.VISIBLE
                timer.start()
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + session.getMobile().toString(),
                    120,
                    TimeUnit.SECONDS,
                    requireActivity(),
                    mCallback!!,
                    code
                )
            }
        })

        val s1 = "One Time Password (OTP) has been sent to your mobile"
        val s2 = ", please enter the same here to login"
        val s = s1 + " XXXXXX" + (mobileNum?.takeLast(4)) + s2
        otpText.text = s


        val auth = FirebaseAuth.getInstance()
        auth.useAppLanguage()
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(vId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(vId, token)
                Log.d("Credentials", vId)
                Log.d("p1", token.toString())
                verificationId = vId
                code = token
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
//                Log.d("OTP", p0.smsCode)
                Log.d("Credentials", p0.toString())
                Log.d("Verification Completed", session.getMobile().toString())
//                session.setVerifiedState(true)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("Error", p0.toString())
                Log.d("Verification Failed", session.getMobile().toString())
//                session.setVerifiedState(false)
            }

        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobileNum",
            120,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallback!!
        )

        otpSignUp.setOnClickListener(View.OnClickListener {
            if (otp.text.isNotEmpty()) {
                val credential =
                    PhoneAuthProvider.getCredential(verificationId!!, otp.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            session.setPhoneCredential(verificationId, otp.text.toString())
                            session.setVerifiedState(true)
                            timer.cancel()
                            proceed()
                        } else {
                            if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    requireContext(),
                                    "Verification Failed, Invalid credentials",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
            }

        })
    }

    private fun proceed() {
        if (session.getVerifiedState()!!) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fml_signin, PasswordFragment())
                .addToBackStack("FragOTP")
                .commit()
        }
    }

}