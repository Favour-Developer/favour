package com.example.favour

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.approval_activity.*
import kotlinx.android.synthetic.main.approval_activity.call
import kotlinx.android.synthetic.main.fragment_process_request.*

class ApprovalActivity : NavigationDrawer() {

    private val REQUEST_CODE = 42
    var mobile: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.approval_activity)

        val s: String = intent.extras!!.get("Request_Object").toString()
        val requestDTO = Gson().fromJson(s, RequestDTO::class.java)

        BackButtonToHome.setOnClickListener(View.OnClickListener {
            onBackPressed()
            finish()
        })

        requestType.text = requestDTO.categories
        if (!requestDTO.urgent) requestUrgent.visibility = View.GONE


        call.setOnClickListener(View.OnClickListener {
            if (askForPermissions() && mobile!=null) {
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:$mobile")
                    )
                )
            }
        })

        var requestProcessDTO: RequestProcessDTO

        val ref =
            FirebaseDatabase.getInstance().reference.child(Session(this).CURRENT_PROCESSING_REQUEST)



        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    requestProcessDTO = i.getValue(RequestProcessDTO::class.java)!!
                    if (requestProcessDTO.requestID == requestDTO.requestID) {
                        amountLayout.visibility = View.VISIBLE
                        quotedAmount.text = requestProcessDTO.amount.toString()
                        getPhone(requestProcessDTO.favourerUID)
                        approveAmount.setBackgroundColor(resources.getColor(R.color.black))
                        break
                    }

                }
            }
        })



//        favourerName.text = requestProcessDTO.toString()

        approveAmount.setOnClickListener(View.OnClickListener {
            if (amountLayout.visibility == View.VISIBLE) {

                ref.addListenerForSingleValueEvent(object : ValueEventListener {
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

    private fun getPhone(favourerUID: String?) {
        FirebaseDatabase.getInstance().reference.child(Session(this).USERS).child(favourerUID!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    mobile = snapshot.getValue(String::class.java)
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