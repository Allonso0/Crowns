package com.example.crowns.domain.logic

import com.example.crowns.domain.model.KillerSudokuBoard

class SudokuValidator {
    /**
     * Функция verifySolution проверяет корректность решения.
     * Проверяет строки, столбцы, блоки 3x3 и суммы клеток.
     * @param board Игровое поле.
     */
    fun verifySolution(board: KillerSudokuBoard): Boolean {
        // Проверка строк и столбцов
        for (i in 0..8) {
            val row = board.cells[i].mapNotNull { it.value }
            val col = board.cells.map { it[i].value }.filterNotNull()
            if (row.toSet().size != row.size || col.toSet().size != col.size) return false
        }

        // Проверка блоков 3x3
        for (blockRow in 0..2) {
            for (blockCol in 0..2) {
                val block = (0..2).flatMap { i ->
                    (0..2).map { j ->
                        board.cells[blockRow * 3 + i][blockCol * 3 + j].value
                    }
                }.filterNotNull()
                if (block.toSet().size != block.size) return false
            }
        }

        // Проверка сумм клеток
        board.cages.forEach { (cageId, cells) ->
            val expectedSum = board.cells[cells[0].first][cells[0].second].cageSum

            val actualSum = cells.sumOf { (i, j) -> board.cells[i][j].value ?: 0 }

            if (actualSum != expectedSum) return false
        }

        return true
    }
}