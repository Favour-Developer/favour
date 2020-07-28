package com.example.favour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_request.*


class RequestFragment : Fragment() {
    val data: MutableList<RequestDTO> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false)
    }
    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        populateData()

        val adapter = RequestRecyclerAdapter(requireContext(),data)
        recyclerView.adapter = adapter
    }

    private fun populateData() {
        data.add(
            RequestDTO(
                "Ritik Gupta",
                "reqshrit1",
                "Grocery, Shopping, Medicine",
                6,
                true,
                0
            )
        )
        data.add(
            RequestDTO(
                "Shadab",
                "reqbosha1",
                "Laptop",
                6,
                true,
                1
            )
        )
        data.add(
            RequestDTO(
                "Shadab",
                "reqbosha2",
                "Zindagi",
                6,
                false,
                1
            )
        )
        data.add(
            RequestDTO(
                "Ritik",
                "reqborit2",
                "PPO",
                6,
                true,
                1
            )
        )
        data.add(
            RequestDTO(
                "Vedant",
                "reqshved1",
                "Personal Care",
                6,
                false,
                0
            )
        )
    }


}