package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_process2.*
import kotlinx.android.synthetic.main.fragment_process_request.*


class FragmentProcess2 : Fragment() {

    var amount: Int = 0
    lateinit var requestId: String
    lateinit var ref: DatabaseReference
    lateinit var listener: ValueEventListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestId = arguments?.getString("requestId").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ref =
            FirebaseDatabase.getInstance().reference.child(Session(requireContext()).CURRENT_PROCESSING_REQUEST)


//        WaitingApproval.setOnClickListener(View.OnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.processChild, FragmentProcess3())
//                .commit()
//        })


        return inflater.inflate(R.layout.fragment_process2, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null && arguments?.containsKey("quoted_amount")!!) {
            amount = arguments?.getInt("quoted_amount")!!
            amountDisplay.text = amount.toString()
        } else {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {

                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.requestID == requestId) {
                            amount = requestProcessDTO.amount
                            amountDisplay.text = amount.toString()
                            break
                        }

                    }
                }
            })
        }

       listener =  ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                    if ((requestProcessDTO!!.requestID == requestId) && (requestProcessDTO.amountApproved)) {
                       startFragment()
//                        view.findViewById<FrameLayout>(R.id.processChild).removeAllViews()

                        break
                    }

                }
            }
        })
    }

    fun startFragment(){
        ref.removeEventListener(listener)
        val bundle = Bundle()
        bundle.putString("requestId", requestId)
        bundle.putInt("quoted_amount", amount)
        val frag3 = FragmentProcess3()
        frag3.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.processChild, frag3)
            .commit()
    }

}