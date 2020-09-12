package com.example.favour

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.favour.notifications.Data
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_notification.*


class FragmentNotification : Fragment() {
    private var data: ArrayList<Data> = ArrayList()
    lateinit var adapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        BackButtonToHome.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
        val database = Session(requireContext()).databaseRoot()
        database.keepSynced(true)
        database.child("notifications")
            .child(FirebaseAuth.getInstance().uid.toString()).child("myNotifications")
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Failed to read", error.toException().toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isAdded) {
                        data.clear()
                        for (snap in snapshot.children) {
                            val notice = snap.getValue(Data::class.java)
                            data.add(0, notice!!)
                        }
                        if (data.size == 0) noNotice.visibility = View.VISIBLE
                        else noNotice.visibility = View.GONE
                        adapter = NotificationAdapter(requireContext(), data)
                        notification_rv.adapter = adapter
                    }
                }

            })

    }


}