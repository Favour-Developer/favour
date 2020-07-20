package com.example.favour

import android.app.Activity
import android.app.Activity.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_favour_request.*
import com.example.favour.REQUEST_CODE as REQUEST_CODE1

private const val REQUEST_CODE=42
class AddFavour: AppCompatActivity() {
    var iscolor = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_favour_request)
        BackButtonToHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        textView4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if (iscolor) {
                    textView4.setBackgroundColor(Color.parseColor("#377118"))
                    textView4.setTextColor(Color.WHITE)
                } else {
                    textView4.setBackgroundColor(Color.parseColor("#FFF"))
                    textView4.setTextColor(Color.BLACK)
                }
                iscolor = !iscolor
            }
        })
    }
}
//
//        PlaceFavourRequest.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//
//        OpenCamera.setOnClickListener {
//
//            startActivityForResult(takePictureIntent, REQUEST_CODE)
//        }
//    }
//}
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode = com.example.favour.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE) as Bitmap
//            ImageView.setImageBitmap(takenImage)
//        } else {
//            super.onActivityResult(requestCode, resultCode, data) as Bitmap
//        }
//    }
//}