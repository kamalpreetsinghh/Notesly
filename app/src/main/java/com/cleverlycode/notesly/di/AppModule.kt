package com.cleverlycode.notesly.di

import android.app.Application
import androidx.room.Room
import com.cleverlycode.notesly.data.local.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNotesDatabase(app: Application): NotesDatabase =
        Room.databaseBuilder(app, NotesDatabase::class.java, "notes_database")
            .fallbackToDestructiveMigration()
            .build()
}