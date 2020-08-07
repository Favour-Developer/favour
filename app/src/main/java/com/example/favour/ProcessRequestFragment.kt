package com.example.favour

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_process_request.*

class ProcessRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_process_request, container, false)
    }

    override fun onViewCreated(
        @NonNull view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = Bundle()
        bundle.putString("Items", ViewRequestFragment.requestDTO.items)
        bundle.putInt("PhotoOrText", ViewRequestFragment.requestDTO.photoOrtext)
        bundle.putString("requestId", ViewRequestFragment.requestDTO.requestID)
        call.setOnClickListener(View.OnClickListener {
            if (askForPermissions()) {
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:" + ViewRequestFragment.requestDTO.requestID.take(10))
                    )
                )
            }
        })

        val frag = FragmentItemList()
        frag.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.itemContainerProcess, frag).commit()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.processChild, FragmentProcess1()).commit()

    }

    private fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.CALL_PHONE
                )
            ) {
                Permssions(requireContext()).showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    Companion.REQUEST_CODE
                )
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
            Companion.REQUEST_CODE -> {
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

    companion object {
        private const val REQUEST_CODE = 42
    }


}