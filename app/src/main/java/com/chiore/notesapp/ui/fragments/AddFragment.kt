package com.chiore.notesapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.CursorWindow
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentAddBinding
import com.chiore.notesapp.data.model.Notes
import com.chiore.notesapp.viewmodel.NotesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    var colors: Int = 0
    val viewModel: NotesViewModel by viewModels()

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

        binding.saveFab.setOnClickListener {
            createNotes(it)
        }
    }

    private fun createNotes(view: View) {
        binding.apply {
            if (titleEt.text.isNotEmpty() || noteEt.text.isNotEmpty()) {
                val title = titleEt.text.toString().trim()
                val note = noteEt.text.toString().trim()

                if (selectedBitmap != null) {
                    val data = Notes(null, title, note, colors, selectedBitmap)
                    viewModel.addNotes(data)
                } else {
                    val data = Notes(null, title, note, colors, null)
                    viewModel.addNotes(data)
                }
                Toast.makeText(
                    requireContext(), "Notes created succssfully", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(), "Make sure the title and note are not empty", Toast.LENGTH_SHORT
                ).show()
            }

            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    intentFromResult?.let {
                        selectedImage = intentFromResult.data
                        try {
                            selectedImage?.let {
                                if (Build.VERSION.SDK_INT < 28) {
                                    selectedBitmap = MediaStore.Images.Media.getBitmap(
                                        requireActivity().contentResolver,
                                        selectedImage
                                    )
                                    binding.addFragmentIv.visibility = View.VISIBLE
                                    binding.addFragmentIv.setImageBitmap(selectedBitmap)
                                } else {
                                    val source =
                                        ImageDecoder.createSource(requireActivity().contentResolver,
                                            selectedImage!!)
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                                    binding.addFragmentIv.visibility = View.VISIBLE
                                    binding.addFragmentIv.setImageBitmap(selectedBitmap)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
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
                colors = 1
                blueDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_blue)
                yellowDot.setImageResource(0)
                redDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            yellowDot.setOnClickListener {
                colors = 2
                yellowDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_yellow)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            redDot.setOnClickListener {
                colors = 3
                redDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_red)
                blueDot.setImageResource(0)
                yellowDot.setImageResource(0)
                tealDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            tealDot.setOnClickListener {
                colors = 4
                tealDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_teal)
                yellowDot.setImageResource(0)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
                purpleDot.setImageResource(0)
            }
            purpleDot.setOnClickListener {
                colors = 5
                purpleDot.setImageResource(R.drawable.ic_done)
                noteEt.setBackgroundResource(R.drawable.edit_txt_background_purple)
                tealDot.setImageResource(0)
                yellowDot.setImageResource(0)
                blueDot.setImageResource(0)
                redDot.setImageResource(0)
            }
        }
    }

    //                    if (intentFromResult != null) {
//                        selectedImage = intentFromResult.data
//                        selectedImage?.let {
//                            val bitmap =
//                                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
//
//                            binding.addFragmentIv.visibility = View.VISIBLE
//                            binding.addFragmentIv.setImageBitmap(bitmap)
////                            binding.addFragmentIv.setImageURI(it)
//                            // bitmapa cevirmek ucun 367 10:40
//                        }
//                    }

}