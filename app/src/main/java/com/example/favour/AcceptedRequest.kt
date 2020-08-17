package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favour.notifications.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_accepted_request.*

class AcceptedRequest : NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted_request)
        val requestIdList = ArrayList<String>()
        FirebaseDatabase.getInstance().reference.child(Session(this).CURRENT_PROCESSING_REQUEST)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.favourerUID == FirebaseAuth.getInstance().uid)
                            requestIdList.add(requestProcessDTO.requestID)
                    }
                }
            })

        val data: ArrayList<RequestDTO> = ArrayList()

        FirebaseDatabase.getInstance().reference.child(Session(this).REQUESTS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestDTO = i.getValue(RequestDTO::class.java)
                        for (j in requestIdList) {
                            if (j == requestDTO!!.requestID) {
                                data.add(requestDTO!!)
                                break
                            }
                        }
                    }
                }
            })

        val adapter = AcceptedRequestAdapter(this, data)
        acceptedRequests_rv.adapter = adapter


    }
}