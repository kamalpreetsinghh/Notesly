package com.cleverlycode.notesly.di

import com.cleverlycode.notesly.data.repository.NotesRepositoryImpl
import com.cleverlycode.notesly.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsNotesRepository(impl: NotesRepositoryImpl): NotesRepository
}