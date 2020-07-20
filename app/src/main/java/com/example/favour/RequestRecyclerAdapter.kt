package com.example.favour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.favour.RequestRecyclerAdapter.PlaceHolder
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
        } else {
            holder.icon?.setImageResource(R.drawable.borrowing)
            holder.shopBor!!.text = "Borrowing"
        }

        holder.personName!!.text = dataList[position].person_name
        val s = StringBuilder()
        s.append(dataList[position].timer).append(" hour")
        holder.timer?.text = s
        holder.categories!!.text = dataList[position].categories
        if (dataList[position].urgent == false) holder.urgent!!.visibility = View.GONE
    }

    inner class PlaceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView? = null
        var personName: TextView? = null
        var categories: TextView? = null
        var timer: TextView? = null
        var shopBor: TextView? = null
        var urgent: TextView? = null

        init {
            icon = itemView.findViewById(R.id.request_icon)
            personName = itemView.findViewById(R.id.person_name)
            categories = itemView.findViewById(R.id.categories)
            timer = itemView.findViewById(R.id.timer)
            shopBor = itemView.findViewById(R.id.shop_bor)
            urgent = itemView.findViewById(R.id.urgent_request)
            itemView.setOnClickListener(View.OnClickListener {
                Toast.makeText(
                    context,
                    dataList[adapterPosition].requestID,
                    Toast.LENGTH_LONG
                ).show()
            })
        }

    }


}
