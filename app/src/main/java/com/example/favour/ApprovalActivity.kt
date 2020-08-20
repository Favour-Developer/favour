package com.example.favour

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.approval_activity.*
import kotlinx.android.synthetic.main.approval_activity.call
import kotlinx.android.synthetic.main.approval_activity.itemDelivered
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ApprovalActivity : NavigationDrawer() {

    private val REQUEST_CODE = 42
    var mobile: String? = null
    lateinit var ref: DatabaseReference
    lateinit var requestDTO: RequestDTO
    lateinit var requestProcessDTO: RequestProcessDTO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.approval_activity)

        val s: String = intent.extras!!.get("Request_Object").toString()
        requestDTO = Gson().fromJson(s, RequestDTO::class.java)

        BackButtonToHome.setOnClickListener(View.OnClickListener {
            onBackPressed()
            finish()
        })

        requestType.text = requestDTO.categories
        if (!requestDTO.urgent) requestUrgent.visibility = View.GONE

//        fillDetails()


        call.setOnClickListener(View.OnClickListener {
            if (askForPermissions() && mobile != null) {
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$mobile")
                    )
                )
            }
        })


        ref = FirebaseDatabase.getInstance().reference

        if (requestDTO.shop_bor == 0) shoppingApproval()
        else borrowingApproval()


    }

    private fun fillDetails() {

    }

    private fun borrowingApproval() {
        waitingLayout.visibility = View.GONE
        approveAmount.visibility = View.GONE
        itemDelivered.visibility = View.VISIBLE
        favourer.text = "Lender"
        ref.child(Session(this).CURRENT_PROCESSING_REQUEST)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        val j = snap.getValue(RequestProcessDTO::class.java)
                        if (j!!.requestID == requestDTO.requestID) {
                            getFavourerName(j.favourerUID)
                            getPhone(j.favourerUID)
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        itemDelivered.setOnClickListener(View.OnClickListener {

            ProcessRequestFragment.ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        val requestProcessDTO = i.getValue(RequestProcessDTO::class.java)
                        if (requestProcessDTO!!.requestID == ProcessRequestFragment.requestID && requestProcessDTO.amountApproved) {
                            val m = HashMap<String, Boolean>()
                            val mp = HashMap<String, Int>()
                            val md = HashMap<String, String>()
                            m["delivered"] = true
                            m["completed"] = true
                            if (requestDTO.shop_bor == 1) mp["points"] = 100
                            else mp["points"] = 50
                            md["date"] = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Date())
                            i.ref.updateChildren(m as Map<String, Any>)
                            i.ref.updateChildren(mp as Map<String, Any>)
                            i.ref.updateChildren(md as Map<String, Any>)
                            break
                        }

                    }
                }
            })
            FirebaseDatabase.getInstance().reference.child(Session(this).REQUESTS)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            val temp = i.getValue(RequestDTO::class.java)
                            if (ProcessRequestFragment.requestID == temp!!.requestID) {
                                val m = HashMap<String, Boolean>()
                                m["isCompleted"] = true
                                i.ref.updateChildren(m as Map<String, Any>)
                                break
                            }
                        }
                    }
                })

            approvalRoot.removeAllViews()
            supportFragmentManager.beginTransaction()
                .replace(R.id.approvalRoot, FragmentFavourCompleted())
                .commit()

        })


    }

    private fun shoppingApproval() {
        ref.child(Session(this).CURRENT_PROCESSING_REQUEST)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (i in snapshot.children) {
                        requestProcessDTO = i.getValue(RequestProcessDTO::class.java)!!
                        if (requestProcessDTO.requestID == requestDTO.requestID) {
                            getPhone(requestProcessDTO.favourerUID)
                            getFavourerName(requestProcessDTO.favourerUID)
                        }
                        if (requestProcessDTO.requestID == requestDTO.requestID && requestProcessDTO.amount != 0) {
                            amountLayout.visibility = View.VISIBLE
                            quotedAmount.text = requestProcessDTO.amount.toString()
                            waitingLayout.visibility = View.GONE
                            approveAmount.setBackgroundColor(resources.getColor(R.color.black))
                            break
                        }

                    }
                }
            })




        approveAmount.setOnClickListener(View.OnClickListener {
            if (amountLayout.visibility == View.VISIBLE) {

                ref.child(Session(this).CURRENT_PROCESSING_REQUEST)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (i in snapshot.children) {
                                val r = i.getValue(RequestProcessDTO::class.java)
                                if (r!!.requestID == requestDTO.requestID) {
                                    val m = HashMap<String, Boolean>()
                                    m["amountApproved"] = true
                                    i.ref.updateChildren(m as Map<String, Any>)
                                    break
                                }

                            }
                        }

                    })

            }
            approvalRoot.removeAllViews()
            supportFragmentManager.beginTransaction()
                .replace(R.id.approvalRoot, FragmentFavourCompleted())
                .commit()

        })
    }

    private fun getFavourerName(favourerUID: String?) {
        ref.child(Session(this).USERS).child(favourerUID!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userDTO = snapshot.getValue(UserDTO::class.java)
                    favourerName.text = userDTO!!.username
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }

    private fun getPhone(favourerUID: String?) {
        FirebaseDatabase.getInstance().reference.child(Session(this).USERS).child(favourerUID!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userDTO = snapshot.getValue(UserDTO::class.java)
                    mobile = userDTO!!.mobile
                }
            })


    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
            ) {
                Permssions(this).showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    REQUEST_CODE
                )
            }
            return false
        }
        return true
    }
}