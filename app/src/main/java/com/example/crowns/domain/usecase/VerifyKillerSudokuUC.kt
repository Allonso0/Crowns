package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.SudokuValidator
import com.example.crowns.domain.repository.IKillerSudokuRepository
import com.example.crowns.domain.model.KillerSudokuBoard
import javax.inject.Inject

class VerifyKillerSudokuUC @Inject constructor(
    private val validator: SudokuValidator
) {
    operator fun invoke(board: KillerSudokuBoard) : Boolean {
        return validator.verifySolution(board)
    }
}