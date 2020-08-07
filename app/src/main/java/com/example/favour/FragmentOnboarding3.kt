package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_onboarding1.*


class FragmentOnboarding3 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BackFragment.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
        next.setOnClickListener(View.OnClickListener {
            Session(requireContext()).setFirstLaunch(false)
            startActivity(Intent(requireContext(), SignUp::class.java))
            requireActivity().finish()
        })
    }
}