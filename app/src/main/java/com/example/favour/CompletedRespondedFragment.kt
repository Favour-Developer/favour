package com.example.favour

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_completed_responded.*

class CompletedRespondedFragment : Fragment() {
    private var data: MutableList<RequestDTO> = ArrayList()
    lateinit var adapter: CompletedRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_responded, container, false)
    }

    override
    fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
    }

    private fun populateData() {
        val database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)
        val requestIdList = ArrayList<String>()
        database.child(Session(requireContext()).CURRENT_PROCESSING_REQUEST)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        val requestProcessDTO = snap.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.favourerUID == FirebaseAuth.getInstance().uid && requestProcessDTO.completed)
                            requestIdList.add(requestProcessDTO.requestID)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        database.child(Session(requireContext()).REQUESTS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Failed to read", error.toException().toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    data.clear()
                    for (snap in snapshot.children) {
                        val requestDTO = snap.getValue(RequestDTO::class.java)
                        for (i in requestIdList) {
                            if (i == requestDTO!!.requestID && requestDTO.isCompleted) {
                                data.add(0, requestDTO)
                                break
                            }
                        }
                    }

                    if (data.size > 0) blankResponded.visibility = View.GONE
                    else blankResponded.visibility = View.VISIBLE
                    adapter = CompletedRequestAdapter(2, requireContext(), data)
                    responded_rv.adapter = adapter

                }

            })
    }


}