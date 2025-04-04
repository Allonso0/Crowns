package com.example.crowns.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.CrownsApplication
import com.example.crowns.R
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.data.database.entity.KillerSudokuState
import com.example.crowns.data.repository.KillerSudokuSettingsRepository
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
import kotlin.math.max

/**
 * Класс KillerSudokuVM - это ViewModel для режима Killer Sudoku. Она отвечает
 * за управление состоянием игры и взаимодействие со слоем domain.
 */
@HiltViewModel
class KillerSudokuVM @Inject constructor(
    private val generateUC: GenerateKillerSudokuUC,
    private val verifyUC: VerifyKillerSudokuUC,
    private val repository: IKillerSudokuRepository,
    private val statisticRepository: KillerSudokuStatsRepository,
    private val settingsRepository: KillerSudokuSettingsRepository,
    private val application: Application
) : ViewModel() {

    // Состояние UI.
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

    // Хранение значения выбранной ячейки
    private val _selectedCellValue = MutableStateFlow<Int?>(null)
    val selectedCellValue: StateFlow<Int?> = _selectedCellValue.asStateFlow()

    // Состояние настроек режима.
    private val _settings = MutableStateFlow<KillerSudokuSettings?>(null)
    val settings: StateFlow<KillerSudokuSettings?> = _settings.asStateFlow()

    // SoundManager для воспроизведения звуков.
    private val soundManager by lazy {
        (application as CrownsApplication).soundManager
    }

    /**
     * Блок инициализации ViewModel. Он загружает настройки игры из репозитория
     * и восстанавливает сохраненное состояние или запускает новую игру.
     */
    init {
        loadSettings()
        viewModelScope.launch {
            val savedState = repository.loadState()
            when {
                savedState == null -> loadNewGame()
                savedState.isGameCompleted -> loadNewGame()
                else -> restoreState(savedState)
            }
        }
    }

    /**
     * Функция loadSettings загружает настройки игры из репозитория.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect {
                _settings.value = it
            }
        }
    }

    /**
     * Функция restoreState восстанавливает состояние из сохраненной сессии.
     * Она загружает решение, подсказки и таймер.
     */
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
     */
    fun loadNewGame() {
        viewModelScope.launch {
            _uiState.value = KillerSudokuUiState.Loading
            _filledCells.clear()
            _hintCells.clear()
            resetTimer()
            incrementStartedGames()

            try {
                // Генерация новой доски и решения.
                val difficulty = settings.value?.difficulty ?: Difficulty.MEDIUM
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

        // Сохраняем значение выбранной ячейки.
        val cell = currentState.board.cells[row][col]
        _selectedCellValue.value = cell.value
    }

    /**
     * Функция onNumberInput отвечает за обработку ввода числа.
     * @param number Введенное число.
     */
    fun onNumberInput(number: Int) {
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.place)
        }

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

    /**
     * Функция saveCurrentState cохраняет текущее состояние игры.
     */
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

    /**
     * Функция checkGameCompletion проверяет условия завершения игры
     * и вызывает соответствующие функции для победы или поражения.
     */
    private fun checkGameCompletion(board: KillerSudokuBoard, score: Int) {
        val currentSettings = _settings.value
        if (currentSettings?.errorLimitEnabled == true && _totalErrors >= 3) {
            handleGameOver()
        } else if (isBoardComplete(board)) {
            handleVictory(score)
        }
    }

    /**
     * Функция isBoardComplete проверяет заполнена ли доска целиком или нет.
     * При этом проверяется, что все ячейки не являются ошибочно поставленными.
     */
    private fun isBoardComplete(board: KillerSudokuBoard): Boolean {
        return board.cells.all { row ->
            row.all { cell ->
                cell.value != null && !cell.isError
            }
        } && verifyUC(board)
    }

    /**
     * Функция handleGameOver обрабатывает завершение игры (поражение).
     * Она сбрасывает прогресс и сохраняет результат в статистику.
     */
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

    /**
     * Функция handleVictory обрабатывает завершение игры (победу).
     * Она сбрасывает прогресс и сохраняет результат в статистику.
     */
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
            _uiState.value = KillerSudokuUiState.Win(
                score = currentScore,
                elapsedTime = _elapsedTime.value
            )
        }
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
        viewModelScope.launch {
            if (settings.value?.soundEnabled == true) {
                soundManager.playSound(R.raw.erase)
            }
        }


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

        _selectedCellValue.value = null
        _uiState.value = currentState.copy(
            board = newBoard
        )
    }

    /**
     * Функция onClearClick обрабатывает нажатие игрока на кнопку "заново".
     */
    fun onClearClick() {
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.replay)
        }

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
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.hint)
        }

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

    /**
     * Функция onExit вызывается при выходе из экрана.
     * Она сохраняет текущий прогресс и останавливает таймер.
     */
    fun onExit() {
        saveCurrentState()
    }

    /**
     * Функция loadSavedState загружает сохраненное состояние.
     */
    fun loadSavedState() {
        viewModelScope.launch {
            val savedState = repository.loadState()
            if (savedState != null && !savedState.isGameCompleted) {
                restoreState(savedState)
            }
        }
    }

    /**
     * Функция startTimer отвечает за запуск внутреигрового таймера.
     */
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

    /**
     * Функция stopTimer отвечает за остановку внутреигрового таймера.
     */
    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * Функция resetTimer отвечает за сброс внутреигрового таймера.
     */
    fun resetTimer() {
        _elapsedTime.value = 0L
    }

    /**
     * Функция incrementStartedGames увеличивает число начатых игр
     * для экрана статистики.
     */
    fun incrementStartedGames() {
        viewModelScope.launch {
            statisticRepository.updateStats {
                copy(startedGames = startedGames + 1)
            }
        }
    }
}