package com.chiore.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.chiore.notesapp.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {

    fun getNotes() = repository.getAllNotes()
}