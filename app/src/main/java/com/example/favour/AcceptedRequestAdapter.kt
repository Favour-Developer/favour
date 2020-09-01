package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class AcceptedRequestAdapter(val context: Context, val dataList: MutableList<RequestDTO>) :
    RecyclerView.Adapter<AcceptedRequestAdapter.AcceptedHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AcceptedHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.accepted_request_single, parent, false)
        return AcceptedHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: AcceptedHolder, position: Int) {
        holder.personName.text = dataList[position].person_name
        holder.categories.text =
            if (dataList[position].shop_bor == 0) dataList[position].categories else dataList[position].items
        var time = ""
        if(dataList[position].timer >= 24){
            time = (dataList[position].timer/24).toString() + " Days "
        }
        time += (dataList[position].timer%24).toString()  + " Hours"
        holder.timeLeft.text = time
        val ref =
            FirebaseDatabase.getInstance().reference.child(Session(context).CURRENT_PROCESSING_REQUEST)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val r = snap.getValue(RequestProcessDTO::class.java)
                    if (r!!.requestID == dataList[position].requestID
                        && r.reminder) {
                        holder.remind.text = "Reminding in " + r.remindingIn + " Hour"
                        holder.remind.setOnClickListener(null)
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }


    inner class AcceptedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView = itemView.findViewById(R.id.requesteeName)
        var categories: TextView = itemView.findViewById(R.id.categories)
        var timeLeft: TextView = itemView.findViewById(R.id.timeLeft)
        var cancel: Button = itemView.findViewById(R.id.cancel_accepted_request)
        var remind: Button = itemView.findViewById(R.id.set_reminder)


        init {

            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, ProcessFlowActivity::class.java)
                intent.putExtra("Request_Object", Gson().toJson(dataList[adapterPosition]))
                context.startActivity(intent)
            })

            remind.setOnClickListener(View.OnClickListener {

                val types = arrayOf("1 Hour", "2 Hour", "4 Hour", "8 Hour", "16 Hour", "24 Hour")
                var selectedTime = "0"
                AlertDialog.Builder(context).setTitle("Set Reminder")
                    .setItems(
                        types
                    ) { dialog, selected ->
                        selectedTime = types[selected]
                        dialog.dismiss()
                        var s = 0
                        if (selectedTime != "0") {
                            when (selected) {
                                0 -> s = 1
                                1 -> s = 2
                                2 -> s = 4
                                3 -> s = 8
                                4 -> s = 16
                                5 -> s = 24
                            }
                            remind.text = "Reminding in $selectedTime"
                            val ref =
                                FirebaseDatabase.getInstance().reference.child(Session(context).CURRENT_PROCESSING_REQUEST)

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (snap in snapshot.children) {
                                        val r = snap.getValue(RequestProcessDTO::class.java)
                                        if (r!!.requestID == dataList[adapterPosition].requestID) {
                                            val m1 = HashMap<String, Boolean>()
                                            val m2 = HashMap<String, Int>()
                                            val m3 = HashMap<String, String>()
                                            m1["reminder"] = true
                                            m2["remindingIn"] = s
                                            m3["notificationTime"] =
                                                (System.currentTimeMillis() + s * 60 * 60 * 1000).toString()
                                            snap.ref.updateChildren(m1 as Map<String, Any>)
                                            snap.ref.updateChildren(m2 as Map<String, Any>)
                                            snap.ref.updateChildren(m3 as Map<String, Any>)
                                            break
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        }
                    }.show()

            })
            cancel.setOnClickListener(View.OnClickListener {
                AlertDialog.Builder(context).setTitle("Request Acceptance Cancellation")
                    .setMessage("Are you sure you want to cancel this?")
                    .setPositiveButton(
                        "Yes",
                        DialogInterface.OnClickListener { dialogInterface, _ ->
                            FirebaseDatabase.getInstance().reference.child(Session(context).REQUESTS)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            val i = snap.getValue(RequestDTO::class.java)
                                            if (i!!.requestID == dataList[adapterPosition].requestID) {
                                                val m = HashMap<String, Boolean>()
                                                m["isProgress"] = false
                                                snap.ref.updateChildren(m as Map<String, Any>)
                                                break
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })
                            FirebaseDatabase.getInstance().reference.child(Session(context).CURRENT_PROCESSING_REQUEST)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (snap in snapshot.children) {
                                            val i = snap.getValue(RequestProcessDTO::class.java)
                                            if (i!!.favourerUID == FirebaseAuth.getInstance().uid
                                                && i.requestID == dataList[adapterPosition].requestID
                                            ) {
                                                snap.ref.removeValue()
                                                break
                                            }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }
                                })
                            dialogInterface.dismiss()
                            context.startActivity(Intent(context, MainActivity::class.java))

                        }).setNegativeButton("No", null).show()


            })


        }

    }


}
