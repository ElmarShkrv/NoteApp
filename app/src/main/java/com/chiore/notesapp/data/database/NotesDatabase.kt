package com.chiore.notesapp.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.chiore.notesapp.data.dao.NotesDao
import com.chiore.notesapp.data.model.Notes

@Database(
    entities = [Notes::class],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun myNotesDao(): NotesDao

}