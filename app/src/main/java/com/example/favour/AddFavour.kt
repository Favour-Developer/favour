package com.example.favour
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_favour.*
import org.w3c.dom.Text

private const val REQUEST_CODE=42
@Suppress("PLUGIN_WARNING")
class AddFavour: NavigationDrawer() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favour)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        PlaceFavourRequest.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

//        val sw = findViewById<Switch>(R.id.switch1)
//        sw?.setOnCheckedChangeListener { _, isChecked ->
//            val msg = if (isChecked){
//                "Yes"
////                switch1.setBackgroundColor(Color.GREEN)
//            }
//            else {
//                "No "
////                switch1.setBackgroundColor(Color.RED)
//            }
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
//            sw.text = msg
//        }

        OpenCamera.setOnClickListener {
            setupPermissions()
            val TakePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(TakePictureIntent, REQUEST_CODE)
            textView5.visibility = View.GONE
            OpenCamera.visibility = View.GONE
            CrossButton.visibility = View.VISIBLE
        }
        CrossButton.setOnClickListener {
            textView5.visibility = View.VISIBLE
            OpenCamera.visibility = View.VISIBLE
            CrossButton.visibility = View.GONE
            imageView.visibility = View.GONE
        }
        // CAMERA SETUP
        // https://pranaybhalerao.wordpress.com/2018/02/11/run-time-permission-in-androidkotlin/
    }
        val CAMERA_REQUEST_CODE=123;
        fun setupPermissions() {
            val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            if (permission!= PackageManager.PERMISSION_GRANTED){
                makeRequest()
            }
        }
        fun makeRequest(){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
        }
        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
        if (requestCode== REQUEST_CODE && resultCode==Activity.RESULT_OK){
            val TakenImage = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(TakenImage)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
}

