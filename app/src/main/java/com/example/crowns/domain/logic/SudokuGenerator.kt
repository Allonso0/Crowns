package com.example.crowns.domain.logic

import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.model.KillerSudokuBoard
import com.example.crowns.domain.model.KillerSudokuCell

class SudokuGenerator {
    /**
     * Константы для настройки генерации:
     * 1. MAX_CELLS_IN_CAGE - максимальный размер клетки. (временно, TODO: будет меняться от сложности ).
     * 2. EMPTY_CELLS_EASY - количество пустых ячеек для лёгкой сложности.
     * 3. EMPTY_CELLS_MEDIUM - количество пустых ячеек для средней сложности.
     * 4. EMPTY_CELLS_HARD - количество пустых ячеек для тяжелой сложности.
     */
    companion object {
        private const val MAX_CELLS_IN_CAGE = 3
        private const val EMPTY_CELLS_EASY = 45
        private const val EMPTY_CELLS_MEDIUM = 57
        private const val EMPTY_CELLS_HARD = 66
    }

    /**
     * Функция generateBoard генерирует игровое поле с учетом сложности.
     * Шаги:
     * 1. Создает валидное решение судоку.
     * 2. Создает клетки с суммами для игрового поля Sudoku.
     * 3. Скрывает часть ячеек в зависимости от сложности.
     * @param difficulty Сложность игры.
     */
    fun generateBoard(difficulty: Difficulty): Pair<KillerSudokuBoard, List<List<Int>>> {
        val solution = generateValidSudoku()
        val (cages, cageMap) = createCages(solution)
        val puzzle = setDifficulty(solution, cages, cageMap, difficulty)
        return Pair(puzzle, solution)
    }

    /**
     * Функция generateValidSudoku генерирует валидное решение для обыкновенного судоку.
     * Шаги:
     * 1. Заполняем диагональные блоки 3x3 случайными уникальными числами.
     * 2. Рекурсивно заполняем оставшиеся ячейки, параллельно проверяя, не нарушаем ли чего.
     */
    private fun generateValidSudoku() : List<List<Int>> {
        val cells = MutableList(9) { MutableList(9) { 0 } }

        for (i in 0 until 9 step 3) {
            fillSquare(cells, i, i)
        }

        if (!fillEntirely(cells, 0, 3)) {
            throw IllegalStateException("Не удалось сгенерировать судоку!")
        }

        return cells
    }

    /**
     * Функция createCages создает клетки для сгенерированного поля Sudoku,
     * превращая его в поле для Killer Sudoku.
     * Алгоритм:
     * 1. Случайно выбираем стартовую ячейку для генерации сетки.
     * 2. Добавляем соседние ячейки, пока не нарушаются правила
     * и пока клетка не достигнет максимального дозволенного размера.
     * 3. Суммируем значения и считаем сумму клетки.
     * @param board Исходное игровое поле обыкновенного судоку.
     */
    private fun createCages(board: List<List<Int>>) : Pair<Map<Int, Int>, Map<Int, List<Pair<Int, Int>>>> {
        val cages = mutableMapOf<Int, Int>()
        val cageMap = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
        var cageId = 0
        val allCells = (0..8).flatMap { x -> (0..8).map { y -> x to y } }.toMutableSet()

        while (allCells.isNotEmpty()) {
            val (x, y) = allCells.random()
            allCells.remove(x to y)
            val currentCage = mutableListOf(x to y)
            var sum = board[x][y]

            while (currentCage.size < MAX_CELLS_IN_CAGE) {
                val neighbors = currentCage.flatMap { (cx, cy) ->
                    listOf(
                        (cx - 1) to cy, (cx + 1) to cy,
                        cx to (cy - 1), cx to (cy + 1)
                    )
                }.filter { it in allCells }

                if (neighbors.isEmpty()) break

                val (nx, ny) = neighbors.random()
                currentCage.add(nx to ny)
                allCells.remove(nx to ny)
                sum += board[nx][ny]
            }

            cages[cageId] = sum
            cageMap[cageId] = currentCage
            cageId++
        }

        return cages to cageMap
    }

    /**
     * Функция setDifficulty обнуляет ячейки в зависимости от сложности.
     * @param solution Исходное решение судоку.
     * @param cages Суммы клеток.
     * @param cageMap Расположение клеток.
     * @param difficulty Сложность игры.
     */
    private fun setDifficulty(
        solution: List<List<Int>>,
        cages: Map<Int, Int>,
        cageMap: Map<Int, List<Pair<Int, Int>>>,
        difficulty: Difficulty
    ): KillerSudokuBoard {
        // Создаем копию решения, чтобы не модифицировать исходное.
        val puzzle = solution.map { row -> row.toMutableList() }

        // Обнуляем ячейки в зависимости от сложности.
        val emptyCells = when (difficulty) {
            Difficulty.EASY -> EMPTY_CELLS_EASY
            Difficulty.MEDIUM -> EMPTY_CELLS_MEDIUM
            Difficulty.HARD -> EMPTY_CELLS_HARD
        }

        repeat(emptyCells) {
            var y: Int
            var x: Int
            do {
                y = (0..8).random()
                x = (0..8).random()
            } while (puzzle[y][x] == 0)
            puzzle[y][x] = 0
        }

        return KillerSudokuBoard(
            cells = puzzle.mapIndexed { i, row ->
                row.mapIndexed { j, value ->
                    val (cageId, cageCells) = cageMap.entries.first { (_, cells) -> i to j in cells }

                    KillerSudokuCell(
                        value = if (value == 0) null else value,
                        isFixed = value != 0,
                        cageId = cageId,
                        cageSum = cageCells.sumOf { (row, col) -> solution[row][col] },
                        isError = false
                    )
                }
            },
            cages = cageMap
        )
    }

    /**
     * Функция fillSquare заполняет квадрат 3x3 случайными уникальными числами.
     * @param board Игровое поле.
     * @param row Начальная строка квадрата.
     * @param col Начальный столбец квадрата.
     */
    private fun fillSquare(board: MutableList<MutableList<Int>>, row: Int, col: Int) {
        val numbers = (1..9).shuffled()
        var idx = 0
        for (i in row until row + 3) {
            for (j in col until col + 3) {
                board[i][j] = numbers[idx++]
            }
        }
    }

    /**
     * Функция fillEntirely рекурсивно заполняет оставшиеся ячейки судоку.
     * @param board Игровое поле.
     * @param col Столбец для заполнения.
     * @param row Строка для заполнения.
     */
    private fun fillEntirely(board: MutableList<MutableList<Int>>, row: Int, col: Int) : Boolean {
        var curRow = row
        var curCol = col

        if (curCol == 9) {
            curRow++
            curCol = 0
            if (curRow == 9) return true
        }

        if (board[curRow][curCol] != 0) {
            return fillEntirely(board, curRow, curCol + 1)
        }

        for (num in 1..9) {
            if (isOkay(board, curRow, curCol, num)) {
                board[curRow][curCol] = num
                if (fillEntirely(board, curRow, curCol + 1)) {
                    return true
                }
                board[curRow][curCol] = 0
            }
        }
        return false
    }

    /**
     * Функция isOkay проверяет, можно ли разместить переданное
     * число в ячейке игрового поля.
     * @param board Игровое поле.
     * @param row Ряд для размещения.
     * @param col Столбец для размещения.
     * @param num Непосредственно число.
     */
    private fun isOkay(board: List<List<Int>>, row: Int, col: Int, num: Int) : Boolean {
        // Проверка строки и столбца
        for (i in 0..8) {
            if (board[row][i] == num || board[i][col] == num) {
                return false
            }
        }

        // Проверка блока 3x3
        val blockRow = (row / 3) * 3
        val blockCol = (col / 3) * 3
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[blockRow + i][blockCol + j] == num) return false
            }
        }
        return true
    }
}