package com.example.favour

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
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
import com.bumptech.glide.load.engine.bitmap_recycle.IntegerArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Tag
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_favour.*
import kotlinx.android.synthetic.main.activity_add_favour.BackButtonToHome
import kotlinx.android.synthetic.main.view_user_request.*
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val REQUEST_CODE = 42

@Suppress("PLUGIN_WARNING", "DEPRECATION")
class AddFavour : NavigationDrawer() {
    private var hashmap = HashMap<String, Boolean>()
    private var photoOrtext:Int = 2
    private var id: Int = 0
    private lateinit var image: Bitmap
    private var imageUri: Uri? = null
    private lateinit var session: Session
    var text: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favour)
        id = intent.getIntExtra("Type", 0)
        textItemsHeader.text = if (id == 0) "Shopping List" else "Borrowing List"
        if (id == 1) {
            selectCategories.visibility = View.GONE
            OpenCamera.visibility = View.GONE
            textOR.visibility = View.GONE
            openEditText.visibility = View.GONE
            textItemsHeader.visibility = View.VISIBLE
            textItems.visibility = View.VISIBLE
            favourType.text = "Borrowing"
        } else favourType.text = "Shopping"

        BackButtonToHome.setOnClickListener {
            super.onBackPressed()
        }
        session = Session(this)
        PlaceFavourRequest.setOnClickListener {

            when {
                photoOrtext == 2 && textItems.text!!.isEmpty() -> {
                    textItems.error = "This can't be blank."
                }
                id == 0 && getCnt() == 0 -> {
                    Toast.makeText(
                        this,
                        "Please select atleast one category.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    createAndUpload()
                }
            }
        }

        val sw = findViewById<Switch>(R.id.urgent_switch)
        sw.text = "No "
        sw?.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Yes" else "No "
            sw.text = msg
        }

        openEditText.setOnClickListener {
            photoOrtext = 2
            photoListLayout.visibility = View.GONE
            OpenCamera.visibility = View.GONE
            textOR.visibility = View.GONE
            textItems.visibility = View.VISIBLE
            textItemsHeader.visibility = View.VISIBLE
        }

        OpenCamera.setOnClickListener {
            photoOrtext = 1
//            Toast.makeText(this, "Image part not added, in process", Toast.LENGTH_SHORT).show()
            setupPermissions()
            val TakePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(TakePictureIntent, REQUEST_CODE)
            textOR.visibility = View.GONE
            openEditText.visibility = View.GONE
            OpenCamera.visibility = View.GONE
        }
        CrossButton.setOnClickListener {
            OpenCamera.visibility = View.VISIBLE
            photoListLayout.visibility = View.GONE
            textOR.visibility = View.VISIBLE
            openEditText.visibility = View.VISIBLE
        }
    }

    private fun createAndUpload() {

        if (photoOrtext == 2) {
            text = textItems.text.toString()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            uploadImage()
        }
        val requestDTO = RequestDTO(
            session.getUsername(),
            session.getMobile(),
            getItemCategories(),
            text,
            6,
            urgent_switch.isChecked,
            id,
            photoOrtext
        )
        val database = FirebaseDatabase.getInstance().reference
        database.child("requests").push().setValue(requestDTO)
    }

    private fun uploadImage() {
        text = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(
            Date()
        )
        val ref = FirebaseStorage.getInstance().reference.child("Requests")
            .child(text)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Image ...")
        progressDialog.show()

        val bitmap = image

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val dat = baos.toByteArray()
        val uploadTask = ref.putBytes(dat)
        uploadTask.addOnFailureListener { exception ->
            Log.d("Error", exception.toString())
            progressDialog.dismiss()
        }.addOnSuccessListener {
            progressDialog.dismiss()
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }
        }.addOnCompleteListener { it ->
            if (it.isSuccessful) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
            .addOnProgressListener { taskSnapshot ->
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded: " + progress.toInt() + "%...")
            }
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
                s += if (count == 0) i
                else {
                    ", $i"
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

    private fun makeRequest() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                image = data.extras?.get("data") as Bitmap
                photoList.setImageBitmap(image)
                photoListLayout.visibility = View.VISIBLE
            }
        }
    }

    @Suppress("DEPRECATION")
    fun change(view: View) {
        val btn = findViewById<Button>(view.id)
        if (btn.textColors.defaultColor == resources.getColor(R.color.black)) {
            btn.setBackgroundResource(R.drawable.tag_selected)
            btn.setTextColor(resources.getColor(R.color.white))
            hashmap[btn.text.toString()] = true
        } else {
            btn.setBackgroundResource(R.drawable.border)
            btn.setTextColor(resources.getColor(R.color.black))
            hashmap.put(btn.text.toString(), false)

        }
    }
}

