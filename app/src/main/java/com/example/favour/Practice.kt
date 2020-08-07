package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.favour.notifications.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_practice.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class Practice : AppCompatActivity() {
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
        val token = Token(FirebaseInstanceId.getInstance().token!!)
        ref.child(firebaseUser!!.uid).setValue(token)

        apiService =
            Client.Client.getClient("https://fcm.googleapis.com/")!!.create(ApiService::class.java)
        send.setOnClickListener(View.OnClickListener {
            FirebaseDatabase.getInstance().reference.child("Tokens")
                .child(FirebaseAuth.getInstance().uid.toString())
                .child("token").addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userToken: String = snapshot.getValue(String::class.java)!!
                        sendNotifications(
                            tokenId.text.toString(),
                            R.drawable.app_icon,
                            "I'm offering you to favour you bro",
                            "Offered"
                        )
                    }
                })

        })
    }

    private fun sendNotifications(userToken: String, appIcon: Int, body: String, title: String) {
        val data = Data(FirebaseAuth.getInstance().uid!!, appIcon, body, title, userToken)
        val sender = Sender(data, Token().getToken())
        apiService.sendNotification(sender).enqueue(object : retrofit2.Callback<MyResponse> {
            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                Toast.makeText(
                    this@Practice,
                    "On Failure, Nothing Happened",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
                if (response.code() == 200) {
                    if (response.body()!!.success !== 1) {
                        Toast.makeText(
                            this@Practice,
                            "Failed, Nothing Happened",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }


        })
    }
}