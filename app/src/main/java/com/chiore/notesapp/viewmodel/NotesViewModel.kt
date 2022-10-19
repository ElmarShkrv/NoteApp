package com.chiore.notesapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chiore.notesapp.data.model.Notes
import com.chiore.notesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository,
) : ViewModel() {


    fun addNotes(notes: Notes) = viewModelScope.launch {
        repository.insertNote(notes)
    }

    fun deleteNote(notes: Notes) = viewModelScope.launch {
        repository.deleteNote(notes)
    }

    fun getNotes() = repository.getAllNotes()

}