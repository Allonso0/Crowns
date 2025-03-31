package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.CrownsValidator
import com.example.crowns.domain.model.CrownsBoard
import javax.inject.Inject

/**
 * VerifyCrownsUC проверяет корректность решения Crowns.
 */
class VerifyCrownsUC @Inject constructor(
    private val validator: CrownsValidator
) {
    operator fun invoke(board: CrownsBoard): Boolean {
        return validator.validate(board)
    }
}