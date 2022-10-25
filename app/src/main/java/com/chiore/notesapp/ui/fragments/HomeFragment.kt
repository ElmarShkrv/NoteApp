package com.chiore.notesapp.ui.fragments

import android.database.CursorWindow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentHomeBinding
import com.chiore.notesapp.adapter.HomeFragmentRvAdapter
import com.chiore.notesapp.viewmodel.NotesViewModel
import com.google.android.material.snackbar.Snackbar
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
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFab.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToAddFragment(null)
            Navigation.findNavController(it).navigate(action)
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

        deleteItem()

    }

    private fun deleteItem() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition

                val deleteNote = homeFragmentRvAdapter.currentList[position]
                val insertNote = homeFragmentRvAdapter.currentList[position]

                viewModel.deleteNote(deleteNote)

                Snackbar.make(requireView(), "Movie deleted", Snackbar.LENGTH_LONG).setAction(
                    "Undo",
                    View.OnClickListener {
                        viewModel.addNotes(insertNote)
                    }
                ).show()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.homeNotesRv)
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