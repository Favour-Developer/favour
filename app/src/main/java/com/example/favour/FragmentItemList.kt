package com.example.favour

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_item_list.*


class FragmentItemList : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        lateinit var items: String
        var photoOrText: Int = 0
        lateinit var requestId: String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        items = arguments?.getString("Items").toString()
        photoOrText = arguments?.getInt("PhotoOrText")!!
        requestId = arguments?.getString("requestId").toString()
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (photoOrText == 1) {
            requestList.visibility = View.GONE
            val ref = FirebaseStorage.getInstance().reference.child("Requests")
                .child(requestId)
            ref.downloadUrl.addOnSuccessListener { uri ->
                Picasso.with(requireContext()).load(uri).into(requestImage)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Coudn't load. Check Intenet!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            requestList.text = items
            requestImage.visibility = View.GONE
        }

    }

}