package com.example.crowns.domain.logic

import com.example.crowns.domain.model.CellState
import com.example.crowns.domain.model.CrownsBoard
import com.example.crowns.domain.model.CrownsCell

class CrownsGenerator {

    /**
     * Функция generateBoard генерирует новое игровое поле заданного размера.
     * Алгоритм:
     * 1. Генерация валидного расположения корон.
     * 2. Формирование регионов вокруг каждой короны.
     * 3. Проверка покрытия всех ячеек регионами.
     */
    fun generateBoard(size: Int): CrownsBoard {
        val solution = generateValidPlacement(size)
        val regions = assignRegions(solution, size)
        val cells = List(size) { row ->
            List(size) { col ->
                CrownsCell(
                    state = CellState.EMPTY,
                    regionID = getRegionIdForCell(regions, row, col)
                )
            }
        }
        return CrownsBoard(size, cells, regions)
    }

    private fun generateValidPlacement(size: Int): List<List<Boolean>> {
        val board = Array(size) { BooleanArray(size) { false } }
        val columns = BooleanArray(size)
        val mainDiag = BooleanArray(2 * size)
        val antiDiag = BooleanArray(2 * size)

        fun backtrack(row: Int): Boolean {
            if (row == size) return true
            val cols = (0 until size).shuffled()
            for (col in cols) {
                val d1 = row - col + size
                val d2 = row + col
                if (!columns[col] && !mainDiag[d1] && !antiDiag[d2]) {
                    board[row][col] = true
                    columns[col] = true
                    mainDiag[d1] = true
                    antiDiag[d2] = true
                    if (backtrack(row + 1)) return true
                    board[row][col] = false
                    columns[col] = false
                    mainDiag[d1] = false
                    antiDiag[d2] = false
                }
            }
            return false
        }

        backtrack(0)
        return board.map { it.toList() }
    }

    private fun assignRegions(solution: List<List<Boolean>>, size: Int): Map<Int, List<Pair<Int, Int>>> {
        val crownPositions = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (solution[i][j]) crownPositions.add(i to j)
            }
        }

        require(crownPositions.size == size) { "Количество корон (${crownPositions.size}) не равно размеру поля ($size)" }

        val cellRegionMap = mutableMapOf<Pair<Int, Int>, Int>()
        val regionCells = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
        val queues = mutableMapOf<Int, ArrayDeque<Pair<Int, Int>>>()

        // Инициализация. Каждая корона является началом региона.
        crownPositions.forEachIndexed { regionId, crown ->
            regionCells[regionId] = mutableListOf(crown)
            queues[regionId] = ArrayDeque(listOf(crown))
            cellRegionMap[crown] = regionId
        }

        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        // Пока есть очереди, продолжаем рост.
        while (queues.any { it.value.isNotEmpty() }) {
            for ((regionId, queue) in queues) {
                if (queue.isEmpty()) continue
                val current = queue.removeFirst()
                val (x, y) = current

                for ((dx, dy) in directions.shuffled()) {
                    val nx = x + dx
                    val ny = y + dy
                    val neighbor = nx to ny

                    if (nx in 0 until size && ny in 0 until size && neighbor !in cellRegionMap) {
                        // Захватываем ячейку.
                        cellRegionMap[neighbor] = regionId
                        regionCells[regionId]?.add(neighbor)
                        queue.add(neighbor)
                    }
                }
            }
        }

        val totalCells = size * size
        require(cellRegionMap.size == totalCells) { "Некоторые ячейки не были распределены по регионам." }

        for ((regionId, cells) in regionCells) {
            val crownCount = cells.count { (i, j) -> solution[i][j] }
            require(crownCount == 1) { "Регион $regionId содержит $crownCount корон (должно быть 1)" }
        }

        return regionCells
    }

    private fun getRegionIdForCell(
        regions: Map<Int, List<Pair<Int, Int>>>,
        row: Int,
        col: Int
    ): Int {
        return regions.entries.firstOrNull { (_, cells) -> cells.contains(row to col) }?.key
            ?: throw IllegalStateException("Ячейка ($row, $col) не найдена ни в одном регионе!")
    }
}