package com.example.crowns.presentation.viewmodel

import com.example.crowns.domain.model.CrownsBoard

/**
 * Состояния UI для экрана Crowns.
 */
sealed interface CrownsUiState {
    // Загрузка данных.
    data object Loading : CrownsUiState

    data class Success(
        val board: CrownsBoard
    ) : CrownsUiState

    data class Error(val message: String) : CrownsUiState

    // Победа.
    data class Win(
        val score: Int, // Итоговый счет.
        val elapsedTime: Long // Итоговое время.
    ) : CrownsUiState
}