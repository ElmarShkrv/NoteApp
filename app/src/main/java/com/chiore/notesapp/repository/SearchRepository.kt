package com.chiore.notesapp.repository

import androidx.lifecycle.LiveData
import com.chiore.notesapp.data.dao.NotesDao
import com.chiore.notesapp.data.model.Notes
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val notesDao: NotesDao
) {
    fun getAllNotes(): LiveData<List<Notes>> {
        return notesDao.getAllNotes()
    }
}