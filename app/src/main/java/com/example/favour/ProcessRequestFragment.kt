package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kotlinx.android.synthetic.main.fragment_item_list.*

class ProcessRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_process_request, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        bundle.putString("Items", ViewRequestFragment.requestDTO.items)
        val frag = FragmentItemList()
        frag.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.itemContainerProcess, frag).commit()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.processChild, FragmentProcess1()).commit()

    }


}