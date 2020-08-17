package com.example.favour

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.favour.notifications.Data
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class NotificationAdapter(val context: Context, val dataList: MutableList<Data>) :
    RecyclerView.Adapter<NotificationAdapter.Holder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notice, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ref = FirebaseStorage.getInstance().reference.child("Profile_photos")
            .child(dataList[position].getUser().toString())
        ref.downloadUrl.addOnSuccessListener { uri ->
            Picasso.with(context).load(uri).into(holder.icon)
        }
        holder.title!!.text = dataList[position].getTitle()
        holder.body!!.text = dataList[position].getBody()

    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView? = null
        var title: TextView? = null
        var body: TextView? = null

        init {
            icon = itemView.findViewById(R.id.userIcon)
            title = itemView.findViewById(R.id.noticeTitle)
            body = itemView.findViewById(R.id.noticeMessage)

            itemView.setOnClickListener(View.OnClickListener {
                val alert: Any = AlertDialog.Builder(context)
                .setTitle("Approve")
                .setMessage("Approve the following user to get your favour done!")
                .setPositiveButton(
                    "Yes"
                ) { dialogInterface, _ ->
                    dialogInterface.cancel()

                }
                .setNegativeButton(
                    "No"
                ) { dialogInterface, _ -> dialogInterface.cancel() }
                .show()
        })
        }

    }


}
