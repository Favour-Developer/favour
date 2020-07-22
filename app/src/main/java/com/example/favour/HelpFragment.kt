package com.example.favour

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment


class HelpFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }
    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val location = view.findViewById<TextView>(R.id.address)
        location.setOnClickListener {
            val geoIntent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "geo:0,0?q="
                            + location.text.toString()
                )
            )
            startActivity(geoIntent)
        }
    }
}