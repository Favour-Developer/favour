package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.favour.notifications.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Picasso


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
        holder.urgency_status.text = if (dataList[position].urgent) "Urgent" else "Not Urgent"
    }


    inner class AcceptedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView = itemView.findViewById(R.id.requesteeName)
        var categories: TextView = itemView.findViewById(R.id.categories)
        var urgency_status: TextView = itemView.findViewById(R.id.urgency_status)
        var cancel: Button = itemView.findViewById(R.id.cancel_accepted_request)
        var remind: Button = itemView.findViewById(R.id.set_reminder)


        init {

            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, ProcessFlowActivity::class.java)
                intent.putExtra("Request_Object", Gson().toJson(dataList[adapterPosition]))
                context.startActivity(intent)
            })
            remind.setOnClickListener(View.OnClickListener {

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
