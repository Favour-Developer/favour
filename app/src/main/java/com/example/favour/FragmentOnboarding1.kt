package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_onboarding1.*


class FragmentOnboarding1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackFragment.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
        next.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.onboarding_container, FragmentOnboarding2())
                .addToBackStack("FragOnboard2").commit()
        })
    }

}