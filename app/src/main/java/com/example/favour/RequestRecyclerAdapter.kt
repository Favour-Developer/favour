package com.example.favour

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.favour.RequestRecyclerAdapter.PlaceHolder
import com.google.gson.Gson
import java.io.Serializable
import java.lang.StringBuilder

class RequestRecyclerAdapter(val context: Context, val dataList: MutableList<RequestDTO>) :
    RecyclerView.Adapter<PlaceHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.request, parent, false)
        return PlaceHolder(v)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        if (dataList[position].shop_bor == 0) {
            holder.icon?.setImageResource(R.drawable.shopping)
            holder.shopBor!!.text = "Shopping"
            holder.categories!!.text = dataList[position].categories
        } else {
            holder.icon?.setImageResource(R.drawable.borrowing)
            holder.shopBor!!.text = "Borrowing"
            holder.categories!!.text = dataList[position].items
        }

        holder.personName!!.text = dataList[position].person_name
        val s = StringBuilder()
        s.append(dataList[position].timer).append(" hour")
        holder.timer?.text = s
        if (dataList[position].isProgress == false) holder.inProgress!!.visibility = View.GONE
        if (dataList[position].urgent == false) holder.urgent!!.visibility = View.GONE
    }


    inner class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView? = null
        var personName: TextView? = null
        var categories: TextView? = null
        var timer: TextView? = null
        var shopBor: TextView? = null
        var urgent: TextView? = null
        var inProgress: TextView? = null

        init {
            icon = itemView.findViewById(R.id.request_icon)
            personName = itemView.findViewById(R.id.person_name)
            categories = itemView.findViewById(R.id.categories)
            timer = itemView.findViewById(R.id.timer)
            shopBor = itemView.findViewById(R.id.shop_bor)
            urgent = itemView.findViewById(R.id.urgent_request)
            inProgress = itemView.findViewById(R.id.inProgress)
            itemView.setOnClickListener(View.OnClickListener {
                val intent = if (dataList[adapterPosition].isProgress) {
                    Intent(context, ApprovalActivity::class.java)
                } else {
                    Intent(context, ProcessFlowActivity::class.java)
                }
                intent.putExtra("Request_Object", Gson().toJson(dataList[adapterPosition]))
                context.startActivity(intent)
            })
        }

    }


}
