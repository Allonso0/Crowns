package com.example.crowns.domain.usecase

import com.example.crowns.domain.logic.CrownsGenerator
import com.example.crowns.domain.model.CrownsBoard
import javax.inject.Inject

/**
 * GenerateCrownsUC генерирует новое игровое поле Crowns.
 */
class GenerateCrownsUC @Inject constructor(
    private val generator: CrownsGenerator
) {
    operator fun invoke(size: Int): CrownsBoard {
        return generator.generateBoard(size)
    }
}