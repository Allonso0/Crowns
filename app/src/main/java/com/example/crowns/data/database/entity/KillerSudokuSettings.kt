package com.example.crowns.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crowns.domain.model.Difficulty

/**
 * Data-класс, представляющий настройки игры Killer Sudoku.
 */
@Entity(tableName = "killer_sudoku_settings")
data class KillerSudokuSettings(
    @PrimaryKey val id: Int = 1,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val showTimer: Boolean = true,
    val errorLimitEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val highlightSameNumbers: Boolean = true,
    val highlightErrors: Boolean = true
)