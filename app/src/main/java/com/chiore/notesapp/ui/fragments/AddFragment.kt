package com.chiore.notesapp.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentAddBinding
import com.chiore.notesapp.data.model.Notes
import com.chiore.notesapp.util.longToast
import com.chiore.notesapp.util.shortToast
import com.chiore.notesapp.viewmodel.NotesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding

    var colors: Int = 0

    val viewModel: NotesViewModel by viewModels()
    val args: AddFragmentArgs by navArgs()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLAuncher: ActivityResultLauncher<String>

    var currentImage: Bitmap? = null
    var currentColor: Int = 0
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

        binding.apply {

            addToolbar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            colorChoose()

            registerLauncher()

            if (args.currentData == null) {
                saveFab.setOnClickListener {
                    createNotes(it)
                }
                addImageIv.setOnClickListener {
                    selectImage(it)
                }
            } else {
                setupUiForEdit()
                saveEditedNote()

                addImageIv.setOnClickListener {
                    selectImage(it)
                }

            }

        }
    }

    private fun saveEditedNote() {

        binding.apply {

            saveFab.setOnClickListener {

                val currentData = args.currentData

                currentData?.let {

                    currentColor = colors

                    if (currentColor == 0) {
                        currentColor = currentData.colors
                    }

                    if (currentImage == null) {
                        currentImage = currentData.noteImage
                    }

                    if (titleEt.text.isNotEmpty() && noteEt.text.isNotEmpty()) {
                        val title = titleEt.text.toString().trim()
                        val note = noteEt.text.toString().trim()

                        val noteData = Notes(currentData.id, title, note, currentColor, currentImage)

                        viewModel.addNotes(noteData)

                        requireContext().longToast("Notes created succssfully")

                        findNavController().navigate(R.id.action_addFragment_to_homeFragment)

                    } else {
                        requireContext().longToast("Make sure the title and note are not empty")
                    }

                }
            }
        }
    }

    private fun setupUiForEdit() {
        binding.apply {
            val currentData = args.currentData

            currentData?.let {

                titleEt.setText(currentData.title)
                noteEt.setText(currentData.notes)

                if (currentData.noteImage != null) {
                    addFragmentIv.visibility = View.VISIBLE
                    Glide.with(root).load(currentData.noteImage).into(addFragmentIv)
                }

                when (currentData.colors) {
                    1 -> {
                        blueDot.setImageResource(R.drawable.ic_done)
                        noteEt.setBackgroundResource(R.drawable.edit_txt_background_blue)
                        yellowDot.setImageResource(0)
                        redDot.setImageResource(0)
                        tealDot.setImageResource(0)
                        purpleDot.setImageResource(0)
                    }
                    2 -> {
                        yellowDot.setImageResource(R.drawable.ic_done)
                        noteEt.setBackgroundResource(R.drawable.edit_txt_background_yellow)
                        blueDot.setImageResource(0)
                        redDot.setImageResource(0)
                        tealDot.setImageResource(0)
                        purpleDot.setImageResource(0)
                    }
                    3 -> {
                        redDot.setImageResource(R.drawable.ic_done)
                        noteEt.setBackgroundResource(R.drawable.edit_txt_background_red)
                        blueDot.setImageResource(0)
                        yellowDot.setImageResource(0)
                        tealDot.setImageResource(0)
                        purpleDot.setImageResource(0)
                    }
                    4 -> {
                        tealDot.setImageResource(R.drawable.ic_done)
                        noteEt.setBackgroundResource(R.drawable.edit_txt_background_teal)
                        yellowDot.setImageResource(0)
                        blueDot.setImageResource(0)
                        redDot.setImageResource(0)
                        purpleDot.setImageResource(0)
                    }
                    5 -> {
                        purpleDot.setImageResource(R.drawable.ic_done)
                        noteEt.setBackgroundResource(R.drawable.edit_txt_background_purple)
                        tealDot.setImageResource(0)
                        yellowDot.setImageResource(0)
                        blueDot.setImageResource(0)
                        redDot.setImageResource(0)
                    }
                    else -> {
                        noteEt.setBackgroundResource(R.color.txt_color)
                    }

                }

            }
        }
    }


    private fun createNotes(view: View) {
        binding.apply {
            if (titleEt.text.isNotEmpty() && noteEt.text.isNotEmpty()) {
                val title = titleEt.text.toString().trim()
                val note = noteEt.text.toString().trim()

                if (selectedBitmap != null) {
                    val data = Notes(null, title, note, colors, selectedBitmap)
                    viewModel.addNotes(data)
                } else {
                    val data = Notes(null, title, note, colors, null)
                    viewModel.addNotes(data)
                }
                requireContext().shortToast("Notes created succssfully")
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
            } else {
                requireContext().shortToast("Make sure the title and note are not empty")
            }
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
                                    currentImage = selectedBitmap

                                    binding.addFragmentIv.visibility = View.VISIBLE
                                    binding.addFragmentIv.setImageBitmap(selectedBitmap)

                                } else {
                                    val source =
                                        ImageDecoder.createSource(requireActivity().contentResolver,
                                            selectedImage!!)
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                                    currentImage = selectedBitmap

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
                    requireContext().longToast("Permission needed!")
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

}