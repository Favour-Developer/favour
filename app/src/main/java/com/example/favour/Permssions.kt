package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.favour.notifications.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class Permssions() {

    lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
    }

    fun sendNotifications(toUid: String, appIcon: Int, body: String, title: String) {
        val apiService =
            Client.Client.getClient("https://fcm.googleapis.com/")!!.create(ApiService::class.java)
        var userToken: String = ""
        val data =
            Data(FirebaseAuth.getInstance().uid!!, appIcon, body, title, toUid)
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
            .child(toUid)
            .child("token")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userToken = snapshot.getValue(String::class.java)!!
                val sender = Sender(data, userToken)
                apiService.sendNotification(sender)
                    .enqueue(object : retrofit2.Callback<MyResponse> {
                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "On Failure, Nothing Happened",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            if (response.code() == 200) {
                                if (response.body()!!.success !== 1) {
                                    Toast.makeText(
                                        context,
                                        "Failed, Nothing Happened",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }


                    })
            }
        })

    }


    public fun showPermissionDeniedDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

}