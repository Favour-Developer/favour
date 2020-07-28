package com.example.favour
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_accept_request.*

class AcceptRequestFragment: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accept_request)
        
        val thirdFragment  = User_Confirmation_fragment()
        SendButton.setOnClickListener {
            SendButton.visibility=View.GONE
            editTextTextPersonName.visibility=View.GONE
            EnterAmount.visibility=View.GONE
            WaitingApproval.visibility=View.VISIBLE
            DisplayAmount.visibility=View.VISIBLE
        }
//        WaitingApproval.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragment,thirdFragment)
//                addToBackStack(null)
//                commit()
//            }
//        }
    }
}
