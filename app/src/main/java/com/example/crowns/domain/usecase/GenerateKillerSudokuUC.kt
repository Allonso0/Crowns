package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.SudokuGenerator
import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.model.KillerSudokuBoard
import javax.inject.Inject

class GenerateKillerSudokuUC @Inject constructor(
    private val generator: SudokuGenerator
) {
    operator fun invoke(difficulty: Difficulty) : Pair<KillerSudokuBoard, List<List<Int>>> {
        return generator.generateBoard(difficulty)
    }
}