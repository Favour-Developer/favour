package com.example.favour

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_process4.*
import kotlinx.android.synthetic.main.fragment_process_request.*


class FragmentProcess4 : Fragment() {

    lateinit var requestId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestId = arguments?.getString("requestId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_process4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        address.text = Session(requireContext()).getAddress()

        val ref =
            FirebaseDatabase.getInstance().reference.child(Session(requireContext()).CURRENT_PROCESSING_REQUEST)

        itemDelivered.setOnClickListener(View.OnClickListener {

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.requestID == requestId && requestProcessDTO.amountApproved) {
                            val m = HashMap<String, Boolean>()
                            m["delivered"] = true
                            m["completed"] = true
                            i.ref.updateChildren(m as Map<String, Any>)
//                            view.findViewById<FrameLayout>(R.id.processChild).removeAllViews()
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.containerProcess, FragmentFavourCompleted())
                                .commit()
                            break
                        }

                    }
                }
            })

        })
    }
}