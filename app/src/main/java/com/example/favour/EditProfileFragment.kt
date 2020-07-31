package com.example.favour

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_edit_profile.*


class EditProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val session = Session(requireContext())
        editEmail.setText(session.getEmail())
        editMobile.setText(session.getMobile())
        editAddress.setText(session.getAddress())

        genderGroup.clearCheck()


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        google_signIn.setOnClickListener(View.OnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, Companion.RC_SIGN_IN)
        })

        update_bio.setOnClickListener(View.OnClickListener {
            session.setAddress(editAddress.text.toString())
            session.setEmail(editEmail.text.toString())
            val radioId = genderGroup.checkedRadioButtonId
            if (radioId != -1) {
                session.setGender(view.findViewById<RadioButton>(radioId).text.toString())
            }
            val fm = requireActivity().supportFragmentManager
            fm.popBackStack("FragEditProfile",FragmentManager.POP_BACK_STACK_INCLUSIVE)
//            fm.popBackStack("FragAccount",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            requireActivity().supportFragmentManager.beginTransaction().remove(this)
                .add(R.id.framelayout, AccountFragment())
                .addToBackStack("FragEditProfile").commit()
        })

    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private lateinit var auth: FirebaseAuth
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
//                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        auth = FirebaseAuth.getInstance()
        // [START_EXCLUDE silent]
//        showProgressBar()
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    editEmail.setText(user!!.email)
//                    DownloadandSaveImage(requireContext()).execute(user.photoUrl.toString())
//                    userImage.setImageURI(user.photoUrl)
                    Log.d("ImageURL", user.photoUrl.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
//                    val view = binding.mainLayout
                    // [END_EXCLUDE]
                    Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT)
                        .show()
//                    updateUI(null)
                }
            }


        // [START_EXCLUDE]
//                hideProgressBar()
        // [END_EXCLUDE]

    }

}
