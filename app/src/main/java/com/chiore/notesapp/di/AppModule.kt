package com.chiore.notesapp.di

import android.content.Context
import androidx.room.Room
import com.chiore.notesapp.data.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun injectRoomDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, NotesDatabase::class.java, "NotesDB"
    ).build()

    @Provides
    @Singleton
    fun injectDao(database: NotesDatabase) = database.myNotesDao()

}