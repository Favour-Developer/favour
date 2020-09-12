package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.favour.notifications.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_accepted_request.*

class AcceptedRequest : NavigationDrawer() {
    lateinit var adapter: AcceptedRequestAdapter
    var data: MutableList<RequestDTO> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted_request)
        val requestIdList = ArrayList<String>()


        BackButtonToHome.setOnClickListener(View.OnClickListener {
            onBackPressed()
            finish()
        })
        val database = Session(this).databaseRoot()
        database.keepSynced(true)
        database.child(Session(this).CURRENT_PROCESSING_REQUEST)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.favourerUID == FirebaseAuth.getInstance().uid && !requestProcessDTO.completed)
                            requestIdList.add(requestProcessDTO.requestID)
                    }
                }
            })


        database.child(Session(this).REQUESTS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    data.clear()
                    for (i in snapshot.children) {
                        val requestDTO = i.getValue(RequestDTO::class.java)
                        for (j in requestIdList) {
                            if (j == requestDTO!!.requestID && !requestDTO.expired) {
                                data.add(0, requestDTO)
                                break
                            }
                        }
                    }
                    if (data.size > 0) blank.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }
            })
        adapter = AcceptedRequestAdapter(this@AcceptedRequest, data)
        acceptedRequests_rv.adapter = adapter

    }
}