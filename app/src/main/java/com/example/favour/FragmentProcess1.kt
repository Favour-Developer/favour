package com.example.favour

import android.R.attr.button
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.favour.ViewRequestFragment.Companion.requestDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_process1.*
import kotlinx.android.synthetic.main.fragment_process_request.*


class FragmentProcess1 : Fragment() {
    lateinit var requestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestId = arguments?.getString("requestId").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_process1, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        editAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.isNotEmpty() && p0.toString().toInt() > 0) {
                    SendButton.setBackgroundColor(resources.getColor(R.color.Green))
                } else {
                    p0.clear()
                    SendButton.setBackgroundColor(resources.getColor(R.color.buttonUnselected))
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


        })

        SendButton.setOnClickListener(View.OnClickListener {

            if (editAmount.text.isNotEmpty()) {
                AlertDialog.Builder(requireContext()).setTitle("Amount Confirmation!")
                    .setMessage("Are you sure, you want to quote ${editAmount.text} as final amount?")
                    .setPositiveButton(
                        "Continue",
                        DialogInterface.OnClickListener { dialogInterface, _ ->


                            val ref =
                                FirebaseDatabase.getInstance().reference.child(Session(requireContext()).CURRENT_PROCESSING_REQUEST)

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {

                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (i in snapshot.children) {
                                        val r = i.getValue(RequestProcessDTO::class.java)
                                        if (r!!.requestID == requestId) {
                                            val m = HashMap<String, Int>()
                                            m["amount"] = editAmount.text.toString().toInt()
                                            i.ref.updateChildren(m as Map<String, Any>)
                                            break
                                        }

                                    }
                                }

                            })


                            dialogInterface.dismiss()
                            val frag2 = FragmentProcess2()
                            val bundle = Bundle()
                            bundle.putString("requestId", requestId)
                            bundle.putInt("quoted_amount", editAmount.text.toString().toInt())
                            frag2.arguments = bundle
//                            val fml = requireView().findViewById<FrameLayout>(R.id.processChild)
//                            fml.removeAllViews()
//                            requireView().findViewById<FrameLayout>(R.id.processChild).removeAllViews()
                           childFragmentManager.popBackStackImmediate()
                            childFragmentManager.popBackStack()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.processChild, frag2).addToBackStack(null)
                                .commit()
                        })
                    .setNegativeButton("Change", null).show()
            }
        })


    }

}