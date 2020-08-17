package com.example.favour

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val session = Session(requireContext())
        name_field.text = session.getUsername()
        email.text = session.getEmail()
        mobile.text = session.getMobile()
        gender.text = session.getGender()
        address.text = session.getAddress()


        if (session.getPhotoUrl() != "") {
            Picasso.with(requireContext())
                .load(session.getPhotoUrl()).into(userImage)
            Log.d("PhotoURl", session.getPhotoUrl().toString())
        }

        Log.i("Current User", FirebaseAuth.getInstance().currentUser?.uid.toString())

        edit_profile.setOnClickListener(View.OnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout, EditProfileFragment())
                .addToBackStack("FragEditProfile").commit()
        })

    }

}