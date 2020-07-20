package com.example.favour
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Path
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_favour_request.*

//private const val REQUEST_CODE=42
class AddFavour: AppCompatActivity() {
    var iscolor = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_favour_request)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        GroceriesButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (iscolor) {
                    GroceriesButton.setBackgroundColor(Color.parseColor("#377118"))
                    GroceriesButton.setTextColor(Color.WHITE)
                } else {
                    GroceriesButton.setBackgroundColor(Color.parseColor("#FFF"))
                    GroceriesButton.setTextColor(Color.BLACK)
                }
                iscolor = !iscolor
            }
        })
//
//        OpenCamera.setOnClickListener {
//            val TakePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            startActivityForResult(TakePictureIntent, REQUEST_CODE)
//
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        if (requestCode== REQUEST_CODE && resultCode==Activity.RESULT_OK){
//            val TakenImage = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(TakenImage)
//        }else{
//            super.onActivityResult(requestCode, resultCode, data)
//        }
     }
}
