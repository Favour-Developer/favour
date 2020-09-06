package com.example.favour

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


@Suppress("DEPRECATION")
class EditProfileFragment : Fragment() {

    private val CHOOSE_IMAGE = 1
    private val CROP_IMAGE = 2
    private lateinit var session: Session
    private var path: String = ""
    lateinit var uri: Uri
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        session = Session(requireContext())
        if (session.getEmail() != "Add Email") editEmail.setText(session.getEmail())
        editMobile.text = session.getMobile()
        if (session.getAddress() != "Add Address") editAddress.setText(session.getAddress())
        if (session.getPhotoUrl() != "")
            Picasso.with(requireContext()).load(session.getPhotoUrl()).into(userImage)

        genderGroup.check(
            when {
                session.getGender() == "Male" -> R.id.male
                session.getGender() == "Female" -> R.id.female
                session.getGender() == "Others" -> R.id.others
                else -> -1
            }
        )

        changePhoto.setOnClickListener(View.OnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            val mimeType = arrayOf("image/jpeg", "image/jpg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(intent, CHOOSE_IMAGE)
        })


        // Configure Google Sign In
        /* Disabling
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        google_signIn.setOnClickListener(View.OnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, Companion.RC_SIGN_IN)
        })
         */

        update_bio.setOnClickListener(View.OnClickListener {
            if (path != "") session.setPhotoUrl(path)
            session.setAddress(editAddress.text.toString())
            session.setEmail(editEmail.text.toString())

            val storageReference: StorageReference = FirebaseStorage.getInstance().reference
                .child("Profile_photos")
                .child(FirebaseAuth.getInstance().uid.toString())
            storageReference.putFile(Uri.parse(path))
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    storageReference.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        session.setPhotoUrl(path)
                        Log.d("URL", downloadUri.toString())
                    } else {
                        Log.d("URL", "Failed")
                    }
                }

            val radioId = genderGroup.checkedRadioButtonId
            if (radioId != -1) {
                session.setGender(view.findViewById<RadioButton>(radioId).text.toString())
            }
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .setValue(
                    UserDTO(
                        session.getUsername().toString(), editEmail.text.toString(),
                        session.getGender().toString(), editAddress.text.toString(),
                        session.getPhotoUrl()!!,
                        session.getMobile().toString()
                    )
                )

            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        })

    }

    companion object {
        private const val RC_SIGN_IN = 9001
        private lateinit var auth: FirebaseAuth
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
/*Disabling
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
//                updateUI(null)
                // [END_EXCLUDE]
            }
        } else
 */
        if (requestCode == CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data!!
                cropImage(imageUri)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val result = CropImage.getActivityResult(data)
                uri = getUri(decodeUri(result.uri))
                path = uri.toString()
                Picasso.with(requireContext()).load(uri).into(userImage)
            }
        }
    }

    private fun cropImage(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(requireContext(), this)
    }

    private fun getUri(image: Bitmap): Uri {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val path: String = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            image,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    @Throws(FileNotFoundException::class)
    fun decodeUri(uri: Uri?): Bitmap {
        val requiredSize = 300
        val c = requireContext()
        val o: BitmapFactory.Options = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri!!), null, o)
        var width_tmp: Int = o.outWidth
        var height_tmp: Int = o.outHeight
        var scale = 1
        while (true) {
            if (width_tmp  < requiredSize || height_tmp < requiredSize) break
            width_tmp /= 2
            height_tmp /= 2
            scale++
        }
        val o2: BitmapFactory.Options = BitmapFactory.Options()
        o2.inSampleSize = scale
        return BitmapFactory.decodeStream(c.contentResolver.openInputStream(uri), null, o2)!!
    }
/*Disabling
    private fun firebaseAuthWithGoogle(idToken: String) {
        auth = FirebaseAuth.getInstance()
        // [START_EXCLUDE silent]
//        showProgressBar()
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    editEmail.setText(user!!.email)
                    uri = user.photoUrl!!
                    path = uri.toString()
                    val temp = path.takeLast(5)
                    path = path.substring(0, path.length - 5) + "s220-c"
                    Picasso.with(requireContext()).load(path).into(userImage)

                    session.setPhotoUrl(path)
//                    DownloadandSaveImage(requireContext()).execute(user.photoUrl.toString())
//                    userImage.setImageURI(user.photoUrl)
                    Log.d("ImageURL", user.photoUrl.toString())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // [START_EXCLUDE]
//                    val view = binding.mainLayout
                    // [END_EXCLUDE]
                    Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT)
                        .show()
//                    updateUI(null)
                }
            }


        // [START_EXCLUDE]
//                hideProgressBar()
        // [END_EXCLUDE]

    }

 */

}
