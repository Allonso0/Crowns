package com.example.crowns.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.data.database.entity.KillerSudokuState
import com.example.crowns.data.repository.KillerSudokuStatsRepository
import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.model.KillerSudokuBoard
import com.example.crowns.domain.repository.IKillerSudokuRepository
import com.example.crowns.domain.usecase.GenerateKillerSudokuUC
import com.example.crowns.domain.usecase.VerifyKillerSudokuUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timer
import kotlin.math.max
import kotlin.math.min

/**
 * Класс KillerSudokuVM - это ViewModel для режима Killer Sudoku. Она отвечает
 * за управление состоянием игры и взаимодействие со слоем domain.
 */
@HiltViewModel
class KillerSudokuVM @Inject constructor(
    private val generateUC: GenerateKillerSudokuUC,
    private val verifyUC: VerifyKillerSudokuUC,
    private val repository: IKillerSudokuRepository,
    private val statisticRepository: KillerSudokuStatsRepository
) : ViewModel() {

    // Состояние UI
    private val _uiState = MutableStateFlow<KillerSudokuUiState>(KillerSudokuUiState.Loading)
    val uiState: StateFlow<KillerSudokuUiState> = _uiState.asStateFlow()

    // Состояние для отслеживания хотя бы раз заполненных ячеек.
    // Это нужно для корректного начисления очков, чтобы убрать "абуз"
    // со стиранием цифры в ячейке и повторной установкой этой же цифры.
    private val _filledCells = mutableSetOf<Pair<Int, Int>>()

    private val _hintCells = mutableSetOf<Pair<Int, Int>>()

    // Правильное решение для проверки правильности ввода.
    private var correctSolution: List<List<Int>> = emptyList()

    // Общее кол-во ошибок
    private var _totalErrors = 0

    // Таймер
    private var _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()
    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            val savedState = repository.loadState()
            when {
                savedState == null -> loadNewGame(Difficulty.MEDIUM)
                savedState.isGameCompleted -> loadNewGame(Difficulty.MEDIUM)
                else -> restoreState(savedState)
            }
        }
    }

    private fun restoreState(savedState: KillerSudokuState) {
        correctSolution = savedState.correctSolution
        _hintCells.addAll(savedState.hintCells)
        _elapsedTime.value = savedState.elapsedTime

        _uiState.value = KillerSudokuUiState.Success(
            board = savedState.board,
            selectedCell = null,
            errorCount = savedState.errorCount,
            score = savedState.score
        )
        _filledCells.addAll(savedState.board.cells
            .flatMapIndexed { x, row ->
                row.mapIndexedNotNull { y, cell ->
                    if (cell.value != null) x to y else null
                }
            }
        )
    }

    /**
     * Функция loadNewGame отвечает за загрузку новой игры с указанной сложностью.
     * @param difficulty Уровень сложности.
     */
    fun loadNewGame(difficulty: Difficulty) {
        viewModelScope.launch {
            _uiState.value = KillerSudokuUiState.Loading
            _filledCells.clear()
            _hintCells.clear()
            resetTimer()
            incrementStartedGames()

            try {
                // Генерация новой доски и решения.
                val (board, solution) = generateUC(difficulty)
                correctSolution = solution

                // Обновляем состояние.
                _uiState.value = KillerSudokuUiState.Success(
                    board = board,
                    selectedCell = null,
                    errorCount = 0,
                    score = 0
                )

                repository.saveState(
                    KillerSudokuState(
                        board = board,
                        score = 0,
                        errorCount = 0,
                        isGameCompleted = false,
                        correctSolution = solution,
                        hintCells = _hintCells.toList(),
                        elapsedTime = elapsedTime.value
                    )
                )
            } catch (e: Exception) { // Обрабатываем ошибки генерации.
                _uiState.value = KillerSudokuUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Функция onCellSelected отвечает за обработку выбора ячейки игроком.
     * @param row Строка выбранной ячейки.
     * @param col Столбец выбранной ячейки.
     */
    fun onCellSelected(row: Int, col: Int) {
        val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return
        _uiState.value = currentState.copy(selectedCell = row to col)
    }

    /**
     * Функция onNumberInput отвечает за обработку ввода числа.
     * @param number Введенное число.
     */
    fun onNumberInput(number: Int) {
        val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return
        val (row, col) = currentState.selectedCell ?: return

        // Проверям, что ячейка доступна для редактирования.
        if (currentState.board.cells[row][col].isFixed) return

        // Засчитана ячейка или нет
        val isNewCell = !_filledCells.contains(row to col)

        // Обновление доски и подсчет ошибок
        val newBoard = updateBoardCell(currentState.board, row, col, number)
        val isCorrect = number == getCorrectValueForCell(row, col)

        val correctValue = getCorrectValueForCell(row, col)
        val isError = (number != correctValue) && (correctValue != 0)

        if (isError && !currentState.board.cells[row][col].isError) {
            _totalErrors++;
        }

        val score = calculateScore(newBoard, row, col, isCorrect, isNewCell)
        val newScore = (currentState.score + score).coerceAtLeast(0)

        // Обновляем состояние.
        if (isCorrect && isNewCell) {
            _filledCells.add(row to col)
        }

        // Обновляем состояние.
        _uiState.value = currentState.copy(
            board = newBoard,
            errorCount = _totalErrors.coerceAtMost(3),
            score = newScore
        )

        saveCurrentState()
        checkGameCompletion(newBoard, score)
    }

    private fun saveCurrentState() {
        val state = _uiState.value as? KillerSudokuUiState.Success ?: return
        viewModelScope.launch {
            repository.saveState(
                KillerSudokuState(
                    board = state.board,
                    score = state.score,
                    errorCount = state.errorCount,
                    isGameCompleted = false,
                    correctSolution = correctSolution,
                    hintCells = _hintCells.toList(),
                    elapsedTime = _elapsedTime.value
                )
            )
        }
    }

    private fun checkGameCompletion(board: KillerSudokuBoard, score: Int) {
        when {
            _totalErrors >= 3 -> handleGameOver()
            isBoardComplete(board) -> handleVictory(score)
        }
    }

    private fun handleGameOver() {
        viewModelScope.launch {
            val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return@launch
            repository.saveState(
                KillerSudokuState(
                    board = currentState.board,
                    score = 0,
                    errorCount = 0,
                    isGameCompleted = true,
                    correctSolution = correctSolution,
                    hintCells = _hintCells.toList(),
                    elapsedTime = _elapsedTime.value
                )
            )
            _uiState.value = KillerSudokuUiState.Lose
        }
    }

    private fun handleVictory(score: Int) {
        viewModelScope.launch {
            val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return@launch
            val currentScore = currentState.score
            val currentTime = _elapsedTime.value

            statisticRepository.updateStats {
                copy(
                    wins = wins + 1,
                    bestScore = max(bestScore, currentScore),
                    bestTime = when {
                        bestTime == 0L -> currentTime
                        currentTime < bestTime -> currentTime
                        else -> bestTime
                    }
                )
            }

            repository.saveState(
                KillerSudokuState(
                    board = currentState.board,
                    score = score,
                    errorCount = 0,
                    isGameCompleted = true,
                    correctSolution = correctSolution,
                    hintCells = _hintCells.toList(),
                    elapsedTime = _elapsedTime.value
                )
            )
            _uiState.value = KillerSudokuUiState.Win(score = currentScore)
        }
    }

    /**
     * Функция calculateErrorCount отвечает за подсчет количества ошибок на доске.
     */
    private fun calculateErrorCount(board: KillerSudokuBoard): Int {
        return board.cells.flatMap { row -> row.filter { it.isError } }.size
    }

    /**
     * Функция updateBoardCell отвечает за обновление ячейки на игровом поле.
     * @param board Игровое поле.
     * @param row Номер строки.
     * @param col Номер столбца.
     * @param number Введенное число.
     */
    private fun updateBoardCell(
        board: KillerSudokuBoard,
        row: Int,
        col: Int,
        number: Int?
    ): KillerSudokuBoard {
        val correctValue = getCorrectValueForCell(row, col)

        val isError = (number != correctValue) && (correctValue != 0)

        return board.copy(
            cells = board.cells.mapIndexed { i, rowCells ->
                rowCells.mapIndexed { j, cell ->
                    if (i == row && j == col) {
                        if (cell.isFixed) cell else cell.copy(
                            value = number.takeIf { it in 1..9 },
                            isError = isError
                        )
                    } else {
                        cell
                    }
                }
            }
        )
    }

    /**
     * Функция getCorrectValueForCell отвечает за получение правильного
     * значения для ячейки из эталонного решения.
     * @param row Номер строки.
     * @param col Номер столбца.
     */
    private fun getCorrectValueForCell(row: Int, col: Int): Int {
        return correctSolution.getOrNull(row)?.getOrNull(col) ?: 0
    }

    /**
     * Функция calculateScore отвечает за подсчёт очков
     * во время игры. После каждого хода игрока счёт пересчитывается.
     * @param row Номер строки установленной ячейки.
     * @param col Номер столбца установленной ячейки.
     * @param isCorrect Верно ли установлена ячейка.
     */
    private fun calculateScore(
        newBoard: KillerSudokuBoard,
        row: Int,
        col: Int,
        isCorrect: Boolean,
        isNewCell: Boolean
    ): Int {
        var score = 0

        // Если ячейка поставлена верно, то прибавляем 50 очков.
        // Если неверно, то отнимаем 200 очков.
        if (isCorrect) {
            if (isNewCell) {
                score += 50

                // Если игрок полностью заполнил ряд, то прибавляем 500 очков.
                if (isRowComplete(newBoard, row)) score += 500
                // Если игрок полностью заполнил колонну, то прибавляем 500 очков.
                if (isColComplete(newBoard, col)) score += 500
                // Если игрок полностью заполнил блок 3x3, то прибавляем 500 очков.
                if (isBlockComplete(newBoard, row, col)) score += 500
            }
        } else  {
            score -= 200
        }

        return score
    }

    /**
     * Функция isRowComplete отвечает за проверку заполнения ряда.
     */
    private fun isRowComplete(board: KillerSudokuBoard, row: Int): Boolean {
        return board.cells[row].all { it.value != null && !it.isError }
    }

    /**
     * Функция isColComplete отвечает за проверку заполнения столбца.
     */
    private fun isColComplete(board: KillerSudokuBoard, col: Int): Boolean {
        return board.cells.all { it[col].value != null && !it[col].isError }
    }

    /**
     * Функция isBlockComplete отвечает за проверку заполнения блока 3x3.
     */
    private fun isBlockComplete(board: KillerSudokuBoard, row: Int, col: Int): Boolean {
        val blockRowStart = (row / 3) * 3
        val blockColStart = (col / 3) * 3

        return (blockRowStart until blockRowStart + 3).all { r ->
            (blockColStart until blockColStart + 3).all { c ->
                board.cells[r][c].value != null && !board.cells[r][c].isError
            }
        }
    }

    /**
     * Функция onEraseClick обрабатывает нажатие игрока на кнопку ластика.
     */
    fun onEraseClick() {
        val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return
        val (row, col) = currentState.selectedCell ?: return

        val cell = currentState.board.cells[row][col]

        if (cell.isFixed || cell.isHint) return

        val newBoard = updateBoardCell(
            board = currentState.board,
            row = row,
            col = col,
            number = null
        )

        _uiState.value = currentState.copy(
            board = newBoard
        )
    }

    /**
     * Функция onClearClick обрабатывает нажатие игрока на кнопку "заново".
     */
    fun onClearClick() {
        val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return

        val newBoard = currentState.board.copy(
            cells = currentState.board.cells.map { row ->
                row.map { cell ->
                    if (cell.isFixed || cell.isHint) cell else cell.copy(value = null, isError = false)
                }
            }
        )

        _filledCells.clear()
        _uiState.value = currentState.copy(
            board = newBoard,
            score = 0
        )
    }

    /**
     * Функция onHintClick обрабатывает нажатие игрока на кнопку подсказки.
     */
    fun onHintClick() {
        val currentState = _uiState.value as? KillerSudokuUiState.Success ?: return

        // Ищем все пустые клетки, доступные для подсказки.
        val emptyCells = currentState.board.cells
            .flatMapIndexed { x, row ->
                row.mapIndexedNotNull { y, cell ->
                    if (cell.value == null && !cell.isFixed) x to y else null
                }
            }

        // Если все клетки заполнены, то незачем давать подсказку :)
        if (emptyCells.isEmpty()) return

        // Выбираем случайную ячейку.
        val (row, col) = emptyCells.random()

        // Получаем правильную цифру.
        val correctValue = getCorrectValueForCell(row, col)

        val newBoard = updateBoardCellWithHint(
            board = currentState.board,
            row = row,
            col = col,
            value = correctValue
        )

        // Обновляем состояние
        _hintCells.add(row to col)
        _uiState.value = currentState.copy(board = newBoard)

        checkGameCompletion(newBoard, currentState.score)
    }

    /**
     * Функция updateBoardCellWithHint обрабатывает обновление ячейки
     * игрового поля в случае, если пользователь использовал подсказку.
     */
    private fun updateBoardCellWithHint(
        board: KillerSudokuBoard,
        row: Int,
        col: Int,
        value: Int
    ): KillerSudokuBoard {
        return board.copy(
            cells = board.cells.mapIndexed { x, rowCells ->
                rowCells.mapIndexed { y, cell ->
                    if (x == row && y == col) {
                        cell.copy(
                            value = value,
                            isHint = true
                        )
                    } else {
                        cell
                    }
                }
            }
        )
    }

    private fun isBoardComplete(board: KillerSudokuBoard): Boolean {
        return board.cells.all { row ->
            row.all { cell ->
                cell.value != null && !cell.isError
            }
        }
    }

    fun onExit() {
        saveCurrentState()
    }

    fun loadSavedState() {
        viewModelScope.launch {
            val savedState = repository.loadState()
            if (savedState != null && !savedState.isGameCompleted) {
                restoreState(savedState)
            }
        }
    }

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedTime.value += 1000
                saveCurrentState()
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        _elapsedTime.value = 0L
    }

    fun incrementStartedGames() {
        viewModelScope.launch {
            statisticRepository.updateStats {
                copy(startedGames = startedGames + 1)
            }
        }
    }
}