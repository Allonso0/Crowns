package com.example.crowns.presentation.viewmodel

import com.example.crowns.domain.model.KillerSudokuBoard

/**
 * Состояния UI для экрана Killer Sudoku.
 */
sealed interface KillerSudokuUiState {
    // Загрузка данных.
    data object Loading : KillerSudokuUiState

    // Успешное состояние.
    data class Success(
        val board: KillerSudokuBoard, // Игровое поле.
        val selectedCell: Pair<Int, Int>?, // Выбранная ячейка (строка, столбец).
        val errorCount: Int, // Количество ошибок.
        val isSolutionValid: Boolean? = null, // Валидность решения.
        val score: Int = 0 // Текущий счет.
    ) : KillerSudokuUiState

    // Ошибка.
    data class Error(val message: String) : KillerSudokuUiState

    // Победа.
    data class Win(
        val score: Int, // Итоговый счет.
        val elapsedTime: Long // Итоговое время.
    ) : KillerSudokuUiState

    // Поражение.
    data object Lose : KillerSudokuUiState
}