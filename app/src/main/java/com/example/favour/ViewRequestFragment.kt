package com.example.favour

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_view_request.*


class ViewRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        lateinit var requestDTO: RequestDTO
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val s = arguments?.getString("RequestObject")
        requestDTO = Gson().fromJson(s, RequestDTO::class.java)

        return inflater.inflate(R.layout.fragment_view_request, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        requestCategory.text = requestDTO.categories
        if (requestDTO.shop_bor != 0) requestType.text = "Borrowing"
        else requestType.text = "Shopping"
        requesteeName.text = requestDTO.person_name

        if (!requestDTO.urgent) requestUrgent.visibility = View.GONE
        val bundle = Bundle()
        bundle.putString("Items", requestDTO.items)
        val frag = FragmentItemList()
        frag.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.itemContainerView, frag).commit()

        val pFrag = ProcessRequestFragment()
        pFrag.arguments = bundle

        acceptRequest.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.containerProcess, pFrag)
                .addToBackStack("ViewRequestFrag").commit()
        })

    }


}