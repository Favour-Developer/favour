package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.fragment_process2.*
import kotlinx.android.synthetic.main.fragment_process3.*

class FragmentProcess3 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_process3, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        approved.setOnClickListener(View.OnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.processChild, FragmentProcess4())
                .commit()

        })
    }
}