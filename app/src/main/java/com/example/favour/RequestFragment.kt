package com.example.favour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_request.*


class RequestFragment : Fragment() {
    private var data: MutableList<RequestDTO> = ArrayList()
    lateinit var adapter: RequestRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
    }

    private fun populateData() {
        val database = FirebaseDatabase.getInstance().reference
        database.keepSynced(true)
        database.child(Session(requireContext()).REQUESTS)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Failed to read", error.toException().toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    data.clear()
                    for (snap in snapshot.children) {
                        val requestDTO = snap.getValue(RequestDTO::class.java)
                         if (requestDTO!!.userUid != FirebaseAuth.getInstance().uid && !requestDTO.isProgress && !requestDTO.isCompleted && !requestDTO.expired)
                            data.add(0, requestDTO)
                    }
                    if (data.size > 0) {
                        blank.visibility = View.GONE
                        adapter = RequestRecyclerAdapter(requireContext(), data)
                        recyclerView.adapter = adapter
                    }
                    else blank.visibility = View.VISIBLE
                }
            })
    }


}