package com.example.crowns.domain.model

data class KillerSudokuSettings(
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val isSoundEnabled: Boolean = true,
    val isTimerEnabled: Boolean = true,
    val isErrorLimitEnabled: Boolean = true,
    val isAutoCheckEnabled: Boolean = true,
    val highlightSameNumbers: Boolean = true,
    val highlightRepeats: Boolean = true
)