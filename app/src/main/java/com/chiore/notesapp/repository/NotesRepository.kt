package com.chiore.notesapp.repository

import androidx.lifecycle.LiveData
import com.chiore.notesapp.data.dao.NotesDao
import com.chiore.notesapp.data.model.Notes
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val notesDao: NotesDao,
) {

    fun getAllNotes(): LiveData<List<Notes>> {
        return notesDao.getAllNotes()
    }

    suspend fun insertNote(notes: Notes) {
        return notesDao.insertNotes(notes)
    }

    suspend fun deleteNote(notes: Notes) {
        return notesDao.deleteNote(notes)
    }

}