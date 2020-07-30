package com.example.favour

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.fragment_process1.*
import kotlinx.android.synthetic.main.fragment_process_request.*


class FragmentProcess1 : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_process1, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

//             if(editAmount.requestFocus())
//        val sv: ScrollView = view.findViewById(R.id.scrollProcess)
//        sv.fullScroll(ScrollView.FOCUS_DOWN)



        editAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty()) {
                    SendButton.setBackgroundColor(R.color.Green)
                } else SendButton.setBackgroundColor(R.color.buttonUnselected)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty()) {
                    SendButton.setBackgroundColor(R.color.Green)
                } else SendButton.setBackgroundColor(R.color.buttonUnselected)
            }
        })

        SendButton.setOnClickListener(View.OnClickListener {
            if (editAmount.text.isNotEmpty()) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.processChild, FragmentProcess2())
                    .commit()
            }
        })


    }

}