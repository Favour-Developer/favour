@file:Suppress("DEPRECATION")

package com.example.favour

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_help.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class HelpFragment : Fragment() {
    private var image: Intent? = null
    private var imageLink: String = ""
    lateinit var d: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        BackButtonToHome.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })
        LaunchOnBoarding.setOnClickListener(View.OnClickListener {

            startActivity(Intent(requireContext(), OnBoarding::class.java))

        })
        selectImage.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(Intent.createChooser(intent, "Select Photo"), 2)
        })

        deleteImage.setOnClickListener(View.OnClickListener {
            imageSelected.visibility = View.GONE
            deleteImage.visibility = View.GONE
            selectImage.visibility = View.VISIBLE
        })

        uploadIssue.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(issueText.text)) {
                val database = FirebaseDatabase.getInstance().reference
                d = database.child("Issues").child(FirebaseAuth.getInstance().uid.toString()).push()
                d.child("Creation_time").setValue(
                    SimpleDateFormat(
                        "yyyyMMdd_HHmmss",
                        Locale.US
                    ).format(
                        Date()
                    )
                )
                d.child("issueText").setValue(issueText.text.toString())
                if (image != null) {
                    report()
                } else requireActivity().onBackPressed()

            } else issueText.error = "This can't be blank."
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {

            if (data != null) {
                image = data
                selectImage.visibility = View.GONE
                imageSelected.visibility = View.VISIBLE
                deleteImage.visibility = View.VISIBLE


            } else image = null
        }
    }

    private fun report() {
        imageLink = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        ).format(
            Date()
        )
        d.child("issueURL").setValue(imageLink)

        val ref = FirebaseStorage.getInstance().reference.child("Issues")
            .child(FirebaseAuth.getInstance().uid.toString()).child(imageLink)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading Image ...")
        progressDialog.show()
        val uri = image?.data

        val bitmap: Bitmap = BitmapFactory.decodeStream(
            context?.contentResolver?.openInputStream(uri!!),
            null,
            BitmapFactory.Options()
        )!!

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
                requireActivity().onBackPressed()
            } else
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
        }
            .addOnProgressListener { taskSnapshot ->
                val progress =
                    (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded: " + progress.toInt() + "%...")
            }
    }
}