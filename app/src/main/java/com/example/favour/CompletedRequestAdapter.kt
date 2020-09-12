package com.example.favour

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class CompletedRequestAdapter(
    private val type: Int,
    val context: Context,
    val dataList: MutableList<RequestDTO>
) :
    RecyclerView.Adapter<CompletedRequestAdapter.CompletedHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CompletedHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_completed_single, parent, false)
        return CompletedHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CompletedHolder, position: Int) {
        holder.personName.text = dataList[position].person_name

        if (dataList[position].shop_bor == 1) {
            holder.amountGroup.visibility = View.GONE
            holder.shopBor.text = "Borrowing"
            holder.categories.text = dataList[position].items
            holder.icon.setImageResource(R.drawable.borrowing)
        } else {
            holder.icon.setImageResource(R.drawable.shopping)
            holder.categories.text = dataList[position].categories
        }

        if (type == 1) {
            holder.icon.visibility = View.VISIBLE
        } else {
            holder.favourPointLayout.visibility = View.VISIBLE
        }


        Session(context).databaseRoot().child(Session(context).CURRENT_PROCESSING_REQUEST)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.requestID == dataList[position].requestID) {
                            holder.amount.text = requestProcessDTO.amount.toString()
                            holder.date.text = requestProcessDTO.date
                            holder.favourPoint.text = requestProcessDTO.points.toString()
                            break
                        }
                    }
                }
            })

    }


    inner class CompletedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var personName: TextView = itemView.findViewById(R.id.person_name)
        var categories: TextView = itemView.findViewById(R.id.categories)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var date: TextView = itemView.findViewById(R.id.date)
        var favourPointLayout: LinearLayout = itemView.findViewById(R.id.favourPointLayout)
        var favourPoint: TextView = itemView.findViewById(R.id.favourPoint)
        var icon: ImageView = itemView.findViewById(R.id.request_icon)
        var shopBor: TextView = itemView.findViewById(R.id.shop_bor)
        var amountGroup: LinearLayout = itemView.findViewById(R.id.amountGroup)


        init {
            itemView.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, ProcessFlowActivity::class.java)
                intent.putExtra("type", type)
                intent.putExtra("Request_Object", Gson().toJson(dataList[adapterPosition]))
                context.startActivity(intent)
                (context as Activity).finish()
            })
        }

    }

}
