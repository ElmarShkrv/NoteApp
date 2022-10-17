package com.chiore.notesapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLAuncher: ActivityResultLauncher<String>

    var selectedImage: Uri? = null
    var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorChoose()

        binding.addImageIv.setOnClickListener {
            selectImage(it)
        }

        registerLauncher()

    }

    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedImage = intentFromResult.data
                        selectedImage?.let {
                            binding.addFragmentIv.visibility = View.VISIBLE
                            binding.addFragmentIv.setImageURI(it)
                            // bitmapa cevirmek ucun 367 10:40
                        }
                    }
                }
            }

        permissionLAuncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(requireContext(), "Permission needed!", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun selectImage(view: View) {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give Permission") {
                        // request permission
                        permissionLAuncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else {
                // request permission
                permissionLAuncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
            // start activity for result
        }
    }

    private fun colorChoose() {
        binding.apply {
            blueDot.setOnClickListener {
                blueDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_blue)
                yellowDot.setImageResource(0)
                redDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            yellowDot.setOnClickListener {
                yellowDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_yellow)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            redDot.setOnClickListener {
                redDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_red)
                blueDot.setImageResource(0)
                yellowDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            tealDot.setOnClickListener {
                tealDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_teal)
                yellowDot.setImageResource(0)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            purpleDot.setOnClickListener {
                purpleDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_purple)
                tealDot.setImageResource(0)
                yellowDot.setImageResource(0)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
            }
        }
    }

}