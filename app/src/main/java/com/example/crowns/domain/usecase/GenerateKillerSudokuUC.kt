package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.SudokuGenerator
import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.model.KillerSudokuBoard
import javax.inject.Inject

/**
 * GenerateKillerSudokuUC генерирует новое игровое поле Killer Sudoku.
 */
class GenerateKillerSudokuUC @Inject constructor(
    private val generator: SudokuGenerator
) {
    // Генерация поля. Возвращает пару игровое поле и правильное решение.
    operator fun invoke(difficulty: Difficulty) : Pair<KillerSudokuBoard, List<List<Int>>> {
        return generator.generateBoard(difficulty) // Запуск генерации.
    }
}