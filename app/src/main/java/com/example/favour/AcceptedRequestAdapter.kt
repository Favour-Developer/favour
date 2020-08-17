package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.favour.notifications.Data
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class AcceptedRequestAdapter(val context: Context, val dataList: MutableList<RequestDTO>) :
    RecyclerView.Adapter<AcceptedRequestAdapter.Holder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.accepted_request_single, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.personName.text = dataList[position].person_name
        holder.categories.text = dataList[position].categories
        holder.urgency_status.text = if (dataList[position].urgent) "Urgent" else "Not Urgent"
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView = itemView.findViewById(R.id.requesteeName)
        var categories: TextView = itemView.findViewById(R.id.categories)
        var urgency_status: TextView = itemView.findViewById(R.id.urgency_status)
        var proceed: Button = itemView.findViewById(R.id.proceed)
        var cancel: Button = itemView.findViewById(R.id.cancel_accepted_request)
        var remind: Button = itemView.findViewById(R.id.set_reminder)


        init {

            proceed.setOnClickListener(View.OnClickListener {

            })
            remind.setOnClickListener(View.OnClickListener {

            })
            cancel.setOnClickListener(View.OnClickListener {

            })


        }

    }


}
