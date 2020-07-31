package com.example.favour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        var cnt = 0;
//        val fm = requireActivity().supportFragmentManager
//        for (i in 0 until fm.backStackEntryCount) {
//            if (fm.getBackStackEntryAt(i).name == "FragAccount") cnt++
//        }
//        if (cnt > 1) fm.popBackStack("FragAccount", FragmentManager.POP_BACK_STACK_INCLUSIVE)

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

        edit_profile.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.framelayout, EditProfileFragment())
                .addToBackStack("FragEditProfile").commit()
        })

    }

}