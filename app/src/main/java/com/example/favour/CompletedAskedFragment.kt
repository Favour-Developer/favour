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
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_completed_asked.*


class CompletedAskedFragment : Fragment() {
    private var data: MutableList<RequestDTO> = ArrayList()
    lateinit var adapter: CompletedRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_completed_asked, container, false)
    }

    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
    }

    private fun populateData() {
        val database = Session(requireContext()).databaseRoot()
        database.keepSynced(true)

        database.child(Session(requireContext()).REQUESTS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Failed to read", error.toException().toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    data.clear()
                    for (snap in snapshot.children) {
                        val requestDTO = snap.getValue(RequestDTO::class.java)
                        if (requestDTO!!.userUid == FirebaseAuth.getInstance().uid && requestDTO.isCompleted)
                            data.add(0, requestDTO)
                    }
                    if (data.size > 0) blankAsked.visibility = View.GONE
                    else blankAsked.visibility = View.VISIBLE
                    adapter = CompletedRequestAdapter(1, requireContext(), data)
                    asked_rv.adapter = adapter

                }

            })
    }

    companion object {
    }
}