package com.example.favour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class Practice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        FirebaseMessaging.getInstance().subscribeToTopic("reminder-notification")
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(this, "You'll get notification", Toast.LENGTH_SHORT).show()
                }
            }
        val jsonObject = JSONObject("{ \n" +
                "    \"to\": \"/topics/reminder-notification\", \n" +
                "    \"priority\": \"high\",\n" +
                "    \"data\" : {\n" +
                "      \"title\" : \"Reminder Notification\",\n" +
                "      \"message\" : \"MESSAGE_HERE\",\n" +
                "      \"isScheduled\" : \"true\",\n" +
                "      \"scheduledTime\" : \"2020-08-24 03:41:00\"\n" +
                "    }\n" +
                "}")


    }
}