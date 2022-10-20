package com.chiore.notesapp.ui.fragments

import android.database.CursorWindow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentHomeBinding
import com.chiore.notesapp.adapter.HomeFragmentRvAdapter
import com.chiore.notesapp.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeFragmentRvAdapter: HomeFragmentRvAdapter
    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        getNotes()
        setupHomeRv()


        /**
         * Row too big to fit into CursorWindow requiredPos=0, totalRows=1
         */
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setupHomeRv() {
        homeFragmentRvAdapter = HomeFragmentRvAdapter()
        binding.apply {
            homeNotesRv.adapter = homeFragmentRvAdapter
        }
    }

    private fun getNotes() {
        viewModel.getNotes().observe(viewLifecycleOwner) { notes ->
            homeFragmentRvAdapter.submitList(notes)
        }
    }

}