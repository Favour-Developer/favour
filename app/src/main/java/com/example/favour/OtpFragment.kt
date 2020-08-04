package com.example.favour

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_front_signin.*
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
        mobileNum = arguments?.getString("Mobile").toString()
        name = arguments?.getString("Name").toString()
        return inflater.inflate(R.layout.fragment_otp, container, false)
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
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() {
                OTPtimer.visibility = View.GONE
                otpResend.setTextColor(resources.getColor(R.color.blue))
            }

            override fun onTick(tick: Long) {
                val s: String = resources.getString(R.string.otp_timer) + (tick/1000).toString()
                OTPtimer.text = s
            }
        }
        timer.start()
        otpResend.setOnClickListener(View.OnClickListener {
            if (otpResend.textColors.defaultColor == resources.getColor(R.color.blue)) {
                otpResend.setTextColor(resources.getColor(R.color.grey))
                OTPtimer.visibility = View.VISIBLE
                timer.start()
            }
        })
        val session = Session(requireContext())
//        var verificationId: String? = null
        val s1 = "One Time Password (OTP) has been sent to your mobile"
        val s2 = ", please enter the same here to login"
        val s = s1 + " XXXXXX" + (mobileNum.takeLast(4)) + s2
        otpText.text = s

        /*
        val auth = FirebaseAuth.getInstance()
        auth.useAppLanguage()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+1" + session.getMobile().toString(),
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(vId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(vId, token)
                    Log.d("Credentials", vId)
                    Log.d("p1", token.toString())
                    verificationId = vId
                }

                override fun onCodeAutoRetrievalTimeOut(p0: String) {
                    super.onCodeAutoRetrievalTimeOut(p0)
                }

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Log.d("Credentials", p0.toString())
                    Log.d("Verification Completed", session.getMobile().toString())
                    session.setVerifiedState(true)

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("Error", p0.toString())
                    Log.d("Verification Failed", session.getMobile().toString())
                    session.setVerifiedState(false)
                }

            }

        )
         */
        otpSignUp.setOnClickListener(View.OnClickListener {
            if ((otp.text.toString() == "123456") || session.getVerifiedState()!!) {
                timer.cancel()
                val bundle = Bundle()
                bundle.putString("Name", name)
                bundle.putString("Mobile", mobileNum)
                val frag = PasswordFragment()
                frag.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fml_signin, frag).addToBackStack("FragOTP")
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Wrong OTP", Toast.LENGTH_LONG).show()
            }


        })
    }

}