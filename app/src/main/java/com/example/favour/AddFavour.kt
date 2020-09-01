@file:Suppress("DEPRECATION")

package com.example.favour

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_favour.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val REQUEST_CODE = 42

@Suppress("PLUGIN_WARNING", "DEPRECATION")
class AddFavour : NavigationDrawer() {
    private var hashmap = HashMap<String, Boolean>()
    private var photoOrtext: Int = 0
    private var id: Int = 0
    private lateinit var image: Bitmap
    private var imageUri: Uri? = null
    private lateinit var imageFile: File
    private lateinit var session: Session
    var toc: String = ""
    var items: String = ""
    var timeLeft: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_favour)
        id = intent.getIntExtra("Type", 0)
        textItemsHeader.text = if (id == 0) "Shopping List" else "Borrowing List"
        toc = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(
            Date()
        )
        if (id == 1) {
            selectCategories.visibility = View.GONE
            OpenCamera.visibility = View.GONE
            textOR.visibility = View.GONE
            openEditText.visibility = View.GONE
            textItemsHeader.visibility = View.VISIBLE
            textItems.visibility = View.VISIBLE
            favourType.text = getString(R.string.borrowing)
            photoOrtext = 2
        } else favourType.text = getString(R.string.shopping)

        BackButtonToHome.setOnClickListener {
            super.onBackPressed()
        }

//        textItemsHeader.setOnClickListener(View.OnClickListener {
////            addFavourScroll.fullScroll(ScrollView.FOCUS_DOWN)
////        })

        session = Session(this)
        PlaceFavourRequest.setOnClickListener {
            when {
                id == 0 && photoOrtext == 0 -> {
                    Toast.makeText(
                        this,
                        "Please select one type of shopping list (Photo list OR text list) ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

        val sw = findViewById<SwitchCompat>(R.id.urgent_switch)
        sw.text = getString(R.string.no)
        sw?.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) getString(R.string.yes) else getString(R.string.no)
            sw.text = msg
        }

        openEditText.setOnClickListener {
            photoOrtext = 2
            openEditText.visibility = View.GONE
            textOR.visibility = View.GONE
            textItems.visibility = View.VISIBLE
            textItemsHeader.visibility = View.VISIBLE
        }

        OpenCamera.setOnClickListener {
            if (askForPermissions()) {
                textItems.visibility = View.GONE
                textItemsHeader.visibility = View.GONE
                textOR.visibility = View.GONE
                openEditText.visibility = View.GONE
                OpenCamera.visibility = View.GONE
                photoOrtext = 1
                imageFile = File.createTempFile(
                    "List_$toc",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(this, "com.example.favour.fileprovider", imageFile)
                )
//                val out = Environment.getExternalStorageDirectory()
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }
        }
        CrossButton.setOnClickListener {
            photoOrtext = 0
            OpenCamera.visibility = View.VISIBLE
            photoListLayout.visibility = View.GONE
            textOR.visibility = View.VISIBLE
            openEditText.visibility = View.VISIBLE
        }
    }

    private fun createAndUpload() {

        if (photoOrtext == 2 || photoOrtext == 0) {
            items = textItems.text.toString()
            onBackPressed()
            finish()
        }
        if (photoOrtext == 1) {
            uploadImage()
        }
        timeLeft = if (urgent_switch.isChecked) 24 else 168
        val requestDTO = RequestDTO(
            session.getUsername(),
            session.getMobile() + "_" + toc,
            FirebaseAuth.getInstance().uid,
            getItemCategories(),
            items,
            System.currentTimeMillis().toString(),
            timeLeft,
            urgent_switch.isChecked,
            id,
            photoOrtext,
            false,
            false,
            false
        )
        val database = FirebaseDatabase.getInstance().reference
        database.child("requests").push().setValue(requestDTO)
    }

    private fun uploadImage() {
        val ref = FirebaseStorage.getInstance().reference.child("Requests")
            .child(session.getMobile() + "_" + toc)
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Image ...")
        progressDialog.show()


        val bitmap = image

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
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
                onBackPressed()
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

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path)
        val orientation: Int =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90F)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180F)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270F)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                imageUri = data.data
//                image = data.extras?.get("data") as Bitmap
            image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(imageFile))
            image = rotateImageIfRequired(image, Uri.fromFile(imageFile))!!
            photoList.setImageBitmap(image)
            photoListLayout.visibility = View.VISIBLE
//            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            photoOrtext = 0
            OpenCamera.visibility = View.VISIBLE
            photoListLayout.visibility = View.GONE
            textOR.visibility = View.VISIBLE
            openEditText.visibility = View.VISIBLE
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
            hashmap[btn.text.toString()] = false

        }
    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this as Activity,
                    android.Manifest.permission.CAMERA
                )
            ) {
                Permssions(this).showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    arrayOf(android.Manifest.permission.CAMERA),
                    REQUEST_CODE
                )
                //return true
            }
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // +
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
            }
        }
    }
}

