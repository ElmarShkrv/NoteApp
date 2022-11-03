package com.chiore.notesapp.ui.fragments


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chiore.noteapp.R
import com.chiore.noteapp.databinding.FragmentSearchBinding
import com.chiore.notesapp.adapter.SearchFragmentRvAdapter
import com.chiore.notesapp.data.model.Notes
import com.chiore.notesapp.util.DefaultItemDecorator
import com.chiore.notesapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchFragmentRvAdapter: SearchFragmentRvAdapter
    val viewModel: SearchViewModel by viewModels()
    var oldNotes = arrayListOf<Notes>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFragmentRvAdapter = SearchFragmentRvAdapter()

        viewModel.getNotes().observe(viewLifecycleOwner) { notes ->
            oldNotes = notes as ArrayList<Notes>
        }

        if (binding.searchEt.requestFocus()) {
            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY)
        }

        var job: Job? = null
        binding.searchEt.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500)
                editable?.let { query ->
                    if (query.toString().isNotEmpty()) {
                        setSearchList(query)
                    }
                }
            }
        }


    }

    private fun setSearchList(query: Editable) {
        val newNotes = arrayListOf<Notes>()

        for (i in oldNotes) {
            if (i.title.contains(query.toString())) {
                newNotes.add(i)
            }
        }

        searchFragmentRvAdapter.submitList(newNotes)
        setupSearchRv()
    }

    private fun setupSearchRv() {
        binding.apply {
            searchRv.adapter = searchFragmentRvAdapter
            searchRv.addItemDecoration(DefaultItemDecorator(
                resources.getDimensionPixelSize(R.dimen.vertical_margin_for_vertical_Search),
                resources.getDimensionPixelSize(R.dimen.vertical_margin_for_horizontal_Search)
            ))
        }
    }

}
