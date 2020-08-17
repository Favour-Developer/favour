package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Practice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)
        val ref = FirebaseDatabase.getInstance().reference.child("requests")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val requestDTO = i.getValue(RequestDTO::class.java)
                    if (requestDTO!!.requestID == "1122334455_20200806_013914") {
                        var m = HashMap<String, Boolean>()
                        m["urgent"] = false

                        i.ref.updateChildren(
                            m as Map<String, Any>
                        )
                    }

                }
            }

        })

        val userDTO = UserDTO(
            "Ritik Gupta",
            "guptaritik97@gmail.com",
            "Female",
            "No address bro. I'm homeless.",
            "",
            Session(this).getMobile().toString()
        )
        FirebaseDatabase.getInstance().reference.child("Users")
            .child(FirebaseAuth.getInstance().uid.toString()).push().setValue(userDTO)

    }
}