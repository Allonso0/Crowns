package com.example.crowns.presentation.viewmodel

import com.example.crowns.domain.model.KillerSudokuBoard

sealed interface KillerSudokuUiState {
    data object Loading : KillerSudokuUiState

    data class Success(
        val board: KillerSudokuBoard,
        val selectedCell: Pair<Int, Int>?,
        val errorCount: Int,
        val isSolutionValid: Boolean? = null,
        val score: Int = 0
    ) : KillerSudokuUiState

    data class Error(val message: String) : KillerSudokuUiState

    data class Win(
        val score: Int,
        val errors: Int
    ) : KillerSudokuUiState

    data class Lose(
        val errors: Int
    ) : KillerSudokuUiState
}