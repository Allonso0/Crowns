package com.example.crowns.di

import com.example.crowns.data.repository.KillerSudokuRepository
import com.example.crowns.domain.logic.SudokuGenerator
import com.example.crowns.domain.logic.SudokuValidator
import com.example.crowns.domain.repository.IKillerSudokuRepository
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
    fun provideSudokuGenerator(): SudokuGenerator = SudokuGenerator()

    @Provides
    @Singleton
    fun provideSudokuValidator(): SudokuValidator = SudokuValidator()

    @Provides
    @Singleton
    fun provideKillerSudokuRepo(): IKillerSudokuRepository = KillerSudokuRepository()
}