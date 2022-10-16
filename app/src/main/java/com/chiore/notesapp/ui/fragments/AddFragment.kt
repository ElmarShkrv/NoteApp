package com.chiore.notesapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {
    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        colorChoose()

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