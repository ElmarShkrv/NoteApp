package com.chiore.notesapp.repository

import androidx.lifecycle.LiveData
import com.chiore.notesapp.dao.NotesDao
import com.chiore.notesapp.model.Notes

class NotesRepository(val dao: NotesDao) {

    fun getAllNotes(): LiveData<List<Notes>> {
        return dao.getAllNotes()
    }

    fun insertNote(notes: Notes) {
        return dao.insertNotes(notes)
    }

    fun deleteNote(notes: Notes) {
        return dao.deleteNote(notes)
    }

}