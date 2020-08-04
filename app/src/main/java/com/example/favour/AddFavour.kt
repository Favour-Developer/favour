package com.example.favour

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Tag
import kotlinx.android.synthetic.main.activity_add_favour.*
import org.w3c.dom.Text
import java.sql.Types.NULL

private const val REQUEST_CODE = 42

@Suppress("PLUGIN_WARNING")
class AddFavour : NavigationDrawer() {
    var hashmap = HashMap<String, Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favour)
        val id = intent.getIntExtra("Type", 0)
        shop_bor_spinner.setSelection(id)
        BackButtonToHome.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
            super.onBackPressed()
        }
        val session = Session(this)
        PlaceFavourRequest.setOnClickListener {
            Log.i("HashMap", hashmap.toString())
            when {
                textItems.text.isEmpty() -> {
                    textItems.error = "This can't be blank."
                }
                getCnt() == 0 -> {
                    Toast.makeText(
                        this,
                        "Please select atleast one category.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val requestDTO = RequestDTO(
                        session.getUsername(),
                        session.getMobile(),
                        getItemCategories(),
                        textItems.text.toString(),
                        6,
                        urgent_switch.isChecked,
                        shop_bor_spinner.selectedItemPosition
                    )
                    createAndUpload(requestDTO)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

        val sw = findViewById<Switch>(R.id.urgent_switch)
        sw.text = "No "
        sw?.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Yes" else "No "
            sw.text = msg
        }

        OpenCamera.setOnClickListener {
            Toast.makeText(this, "Image part not added, in process", Toast.LENGTH_SHORT)
//            setupPermissions()
//            val TakePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            startActivityForResult(TakePictureIntent, REQUEST_CODE)
//            textView5.visibility = View.GONE
//            OpenCamera.visibility = View.GONE
        }
        CrossButton.setOnClickListener {
            textView5.visibility = View.VISIBLE
            OpenCamera.visibility = View.VISIBLE
            CrossButton.visibility = View.GONE
            imageView.visibility = View.GONE

        }
    }

    private fun createAndUpload(requestDTO: RequestDTO) {
        val database = FirebaseDatabase.getInstance().reference
        database.child("requests").push().setValue(requestDTO)
    }

    private fun getItemCategories(): String? {
        var s: String = ""
        if (getCnt() == 1) {
            for ((i, j) in hashmap) {
                if (j) return i
            }
        }
        var count = 0
        for ((i, j) in hashmap) {

            if (j) {
                if (count == 0) s += i
                else {
                    s += ", $i"
                }
                count++

            }
        }

        return s
    }


    private fun getCnt(): Int {
        var cnt = 0
        for (v in hashmap.values) {
            if (v) cnt++
        }
        return cnt
    }

// CAMERA SETUP
// https://pranaybhalerao.wordpress.com/2018/02/11/run-time-permission-in-androidkotlin/

    val CAMERA_REQUEST_CODE = 123;
    fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val TakenImage = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(TakenImage)
            CrossButton.visibility = View.VISIBLE
            imageView.visibility = View.VISIBLE
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    @Suppress("DEPRECATION")
    fun change(view: View) {
        val btn = findViewById<Button>(view.id)
        if (btn.textColors.defaultColor == resources.getColor(R.color.black)) {
            btn.setBackgroundResource(R.drawable.tag_selected)
            btn.setTextColor(resources.getColor(R.color.white))
            hashmap.put(btn.text.toString(), true)
        } else {
            btn.setBackgroundResource(R.drawable.border)
            btn.setTextColor(resources.getColor(R.color.black))
            hashmap.put(btn.text.toString(), false)

        }
    }
}

