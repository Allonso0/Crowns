package com.example.crowns.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crowns.domain.model.KillerSudokuBoard

/**
 * Data-класс, представляющий состояние игрового поля Killer Sudoku.
 */
@Entity(tableName = "killer_sudoku_states")
data class KillerSudokuState(
    @PrimaryKey val id: Int = 1,
    val board: KillerSudokuBoard,
    val score: Int,
    val errorCount: Int,
    val isGameCompleted: Boolean,
    val correctSolution: List<List<Int>>,
    val hintCells: List<Pair<Int, Int>>,
    val elapsedTime: Long = 0
)