package com.example.crowns.domain.repository

import com.example.crowns.data.database.entity.KillerSudokuState

/**
 * Интерфейс репозитория для работы с состоянием игры Killer Sudoku.
 */
interface IKillerSudokuRepository {
    // Сохранение состояния.
    suspend fun saveState(state: KillerSudokuState)

    // Загрузка состояния.
    suspend fun loadState(): KillerSudokuState?
}