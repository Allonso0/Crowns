package com.example.crowns.domain.logic

import com.example.crowns.domain.model.CellState
import com.example.crowns.domain.model.CrownsBoard

class CrownsValidator {

    /**
     * Функция validate проверяет валидность текущего состояния игрового поля.
     * Алгоритм проверки:
     * 1. Проверка строк: каждая строка содержит ровно одну корону.
     * 2. Проверка столбцов: каждый столбец содержит ровно одну корону.
     * 3. Проверка диагоналей: главные и побочные диагонали без конфликтов.
     * 4. Проверка регионов: каждый цветной регион содержит ровно одну корону.
     */
    fun validate(board: CrownsBoard): Boolean {
        val totalCrowns = board.cells.flatten().count { it.state == CellState.CROWN }
        if (totalCrowns != board.size) return false

        for (i in 0 until board.size) {
            val rowCount = board.cells[i].count { it.state == CellState.CROWN }
            val colCount = board.cells.map { row -> row[i] }.count { it.state == CellState.CROWN }
            if (rowCount != 1 || colCount != 1) return false
        }

        val regionCounts = mutableMapOf<Int, Int>()
        board.cells.forEach { row ->
            row.forEach { cell ->
                if (cell.state == CellState.CROWN) {
                    regionCounts[cell.regionID] = (regionCounts[cell.regionID] ?: 0) + 1
                }
            }
        }
        if (regionCounts.any { it.value != 1 }) return false

        for (i in 0 until board.size) {
            for (j in 0 until board.size) {
                if (board.cells[i][j].state == CellState.CROWN) {
                    for (dx in -1..1) {
                        for (dy in -1..1) {
                            if (dx == 0 && dy == 0) continue
                            val x = i + dx
                            val y = j + dy
                            if (x in 0 until board.size && y in 0 until board.size) {
                                if (board.cells[x][y].state == CellState.CROWN) return false
                            }
                        }
                    }
                }
            }
        }

        return true
    }
}