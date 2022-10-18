package com.chiore.notesapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chiore.notesapp.database.NotesDatabase
import com.chiore.notesapp.model.Notes
import com.chiore.notesapp.repository.NotesRepository

class NotesViewModel(application: Application) : ViewModel() {

    val repository: NotesRepository

    init {
        val dao = NotesDatabase.getDatabaseInstance(application).myNotesDao()
        repository = NotesRepository(dao)
    }

    fun addNotes(notes: Notes) {
        repository.insertNote(notes)
    }

    fun deleteNote(notes: Notes) {
        repository.deleteNote(notes)
    }

    fun getNotes(): LiveData<List<Notes>> = repository.getAllNotes()

}