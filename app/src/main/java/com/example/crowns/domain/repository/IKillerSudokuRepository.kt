package com.example.crowns.domain.repository

import com.example.crowns.data.database.entity.KillerSudokuState

interface IKillerSudokuRepository {
    suspend fun saveState(state: KillerSudokuState)
    suspend fun loadState(): KillerSudokuState?
}