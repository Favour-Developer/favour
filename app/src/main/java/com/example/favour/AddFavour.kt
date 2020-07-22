package com.example.favour
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_favour.*
private const val REQUEST_CODE=42
@Suppress("PLUGIN_WARNING")
class AddFavour: AppCompatActivity() {
    var iscolor = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favour)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        PlaceFavourRequest.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))

        }
        val sw = findViewById<Switch>(R.id.switch1)
        sw?.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Yes" else "No"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            sw.text = msg
        }
//        GroceriesButton.setOnClickListener {
//            if (iscolor) {
//                GroceriesButton.setBackgroundColor(Color.parseColor("#377118"))
//                GroceriesButton.setTextColor(Color.WHITE)
//            } else {
//                GroceriesButton.setBackgroundColor(Color.parseColor("#FFF"))
//                GroceriesButton.setTextColor(Color.BLACK)
//            }
//            iscolor = !iscolor
//        }
//
        OpenCamera.setOnClickListener {
            val TakePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(TakePictureIntent, REQUEST_CODE)
            textView5.visibility= View.GONE
            OpenCamera.visibility=View.GONE
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