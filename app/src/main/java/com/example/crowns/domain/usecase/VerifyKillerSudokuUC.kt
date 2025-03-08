package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.SudokuValidator
import com.example.crowns.domain.repository.IKillerSudokuRepository
import com.example.crowns.domain.model.KillerSudokuBoard
import javax.inject.Inject

/**
 * VerifyKillerSudokuUC проверяет корректность решения Killer Sudoku.
 */
class VerifyKillerSudokuUC @Inject constructor(
    private val validator: SudokuValidator // Внедрение зависимости валидатора.
) {
    operator fun invoke(board: KillerSudokuBoard) : Boolean {
        // Делегирование логики валидатору.
        return validator.verifySolution(board)
    }
}