package com.example.favour

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.favour.notifications.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_view_request.*
import retrofit2.Call
import retrofit2.Response


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ViewRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        lateinit var requestDTO: RequestDTO
        lateinit var apiService: ApiService
        lateinit var s: String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        s = arguments?.getString("RequestObject")!!
        requestDTO = Gson().fromJson(s, RequestDTO::class.java)
        apiService =
            Client.Client.getClient("https://fcm.googleapis.com/")!!.create(ApiService::class.java)
        return inflater.inflate(R.layout.fragment_view_request, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        BackButtonToHome.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })

        requestCategory.text = requestDTO.categories
        if (requestDTO.shop_bor != 0) {
            categoryLayout.visibility = View.GONE
            requestType.text = "Borrowing"
        } else requestType.text = "Shopping"
        requesteeName.text = requestDTO.person_name

        if (!requestDTO.urgent) requestUrgent.visibility = View.GONE
        val bundle = Bundle()
        bundle.putInt("PhotoOrText", requestDTO.photoOrtext)
        bundle.putString("Items", requestDTO.items)
        bundle.putString("requestId", requestDTO.requestID)
        val frag = FragmentItemList()
        frag.arguments = bundle

        childFragmentManager.beginTransaction()
            .add(R.id.itemContainerView, frag).commit()

        bundle.putString("RequestObject", s)

        val pFrag = ProcessRequestFragment()
        pFrag.arguments = bundle


        if (requestDTO.requestID.take(10) == Session(requireContext()).getMobile()) acceptRequest.visibility =
            View.GONE


        acceptRequest.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(requireContext()).setTitle("Confirmation")
                .setMessage("Continue to accept the current Favour request?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, _ ->
                    sendNotifications(
                        requestDTO.userUid,
                        R.mipmap.app_icon,
                        Session(requireContext()).getUsername() + " offered to favour you. Make sure to stay active for updates",
                        "Request Accepted!"
                    )
                    dialogInterface.dismiss()

                    val requestProcessDTO = RequestProcessDTO(
                        requestDTO.requestID,
                        FirebaseAuth.getInstance().uid.toString(),
                        true,
                        0,
                        false,
                        false,
                        false,
                        false,
                        false
                    )
                    val ref = FirebaseDatabase.getInstance().reference

                    ref.child(Session(requireContext()).CURRENT_PROCESSING_REQUEST).push()
                        .setValue(requestProcessDTO)

                    ref.child(Session(requireContext()).REQUESTS)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {

                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (i in snapshot.children) {
                                    val temp = i.getValue(RequestDTO::class.java)
                                    if (temp!!.requestID == requestDTO.requestID) {
                                        val m = HashMap<String, Boolean>()
                                        m["isProgress"] = true
                                        i.ref.updateChildren(m as Map<String, Any>)
                                        requireActivity().supportFragmentManager.beginTransaction()
                                            .replace(R.id.containerProcess, pFrag)
                                            .addToBackStack("ViewRequestFrag").commit()
                                        break
                                    }
                                }
                            }
                        })


                })
                .setNegativeButton("No", null).show()

        })

    }

    private fun sendNotifications(toUid: String, appIcon: Int, body: String, title: String) {
        var userToken: String = ""
        val data =
            Data(FirebaseAuth.getInstance().uid!!, appIcon, body, title, toUid)
        val ref = FirebaseDatabase.getInstance().reference.child("Tokens")
            .child(toUid)
            .child("token")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userToken = snapshot.getValue(String::class.java)!!
                val sender = Sender(data, userToken)
                apiService.sendNotification(sender)
                    .enqueue(object : retrofit2.Callback<MyResponse> {
                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "On Failure, Nothing Happened",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse>
                        ) {
                            if (response.code() == 200) {
                                if (response.body()!!.success !== 1) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Failed, Nothing Happened",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }


                    })
            }
        })

    }


}