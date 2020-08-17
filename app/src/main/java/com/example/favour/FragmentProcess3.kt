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
import kotlinx.android.synthetic.main.fragment_process1.*
import kotlinx.android.synthetic.main.fragment_process2.*
import kotlinx.android.synthetic.main.fragment_process3.*
import kotlinx.android.synthetic.main.fragment_process3.purchased
import kotlinx.android.synthetic.main.fragment_process_request.*

class FragmentProcess3 : Fragment() {

    lateinit var requestId: String
    var amount: Int = 0
    lateinit var ref: DatabaseReference



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

        return inflater.inflate(R.layout.fragment_process3, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null && arguments?.containsKey("quoted_amount")!!) {
            amount = arguments?.getInt("quoted_amount")!!
            DisplayAmount.text = amount.toString()
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
        purchased.setOnClickListener(View.OnClickListener {

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.requestID == requestId && requestProcessDTO.amountApproved) {
                            val m = HashMap<String, Boolean>()
                            m["purchased"] = true
                            i.ref.updateChildren(m as Map<String, Any>)
                            val bundle = Bundle()
                            bundle.putString("requestId", requestId)
                            val frag4 = FragmentProcess4()
                            frag4.arguments = bundle
//                            view.findViewById<FrameLayout>(R.id.processChild).removeAllViews()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.processChild, frag4)
                                .commit()
                            break
                        }

                    }
                }
            })

//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.processChild, FragmentProcess4())
//                .commit()


        })


    }
}