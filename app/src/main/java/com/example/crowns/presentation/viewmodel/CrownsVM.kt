package com.example.crowns.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.CrownsApplication
import com.example.crowns.R
import com.example.crowns.data.database.entity.CrownsSettings
import com.example.crowns.data.database.entity.CrownsState
import com.example.crowns.data.repository.CrownsRepository
import com.example.crowns.data.repository.CrownsSettingsRepository
import com.example.crowns.data.repository.CrownsStatsRepository
import com.example.crowns.domain.model.CellState
import com.example.crowns.domain.model.CrownsBoard
import com.example.crowns.domain.model.CrownsCell
import com.example.crowns.domain.usecase.GenerateCrownsUC
import com.example.crowns.domain.usecase.VerifyCrownsUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class CrownsVM @Inject constructor(
    private val generator: GenerateCrownsUC,
    private val repository: CrownsRepository,
    private val validator: VerifyCrownsUC,
    private val statsRepository: CrownsStatsRepository,
    private val settingsRepository: CrownsSettingsRepository,
    private val application: Application
) : ViewModel() {

    // Состояние UI.
    private val _uiState = MutableStateFlow<CrownsUiState>(CrownsUiState.Loading)
    val uiState: StateFlow<CrownsUiState> = _uiState.asStateFlow()

    // Таймер
    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()
    private var timerJob: Job? = null
    private var lastSaveTime: Long = 0L

    // Состояние настроек режима.
    private val _settings = MutableStateFlow<CrownsSettings?>(null)
    val settings: StateFlow<CrownsSettings?> = _settings.asStateFlow()

    // SoundManager для воспроизведения звуков.
    private val soundManager by lazy {
        (application as CrownsApplication).soundManager
    }

    /**
     * Блок инициализации ViewModel. Он загружает настройки игры из репозитория
     * и восстанавливает сохраненное состояние или запускает новую игру.
     */
    init {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { _settings.value = it }
        }

        loadSaveState()
    }

    /**
     * Функция loadSaveState загружает сохраненное состояние игры из репозитория.
     * Если сохраненной игры нет, то запускает новую.
     */
    fun loadSaveState() {
        viewModelScope.launch {
            val savedState = repository.loadState()
            if (savedState == null || savedState.isGameCompleted) {
                generateNewBoard()
            } else {
                restoreState(savedState)
                startTimer(savedState.elapsedTime)
            }
        }
    }

    /**
     * Функция onNewLevelClick обрабатывает нажатие на кнопку "Новый уровень".
     */
    fun onNewLevelClick() {
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.hint)
        }

        generateNewBoard()
    }

    /**
     * Функция restoreState восстанавливает состояние из сохраненных данных.
     */
    private fun restoreState(state: CrownsState) {
        val cellsWithRegions = state.cells.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, cellState ->
                val regionId = state.regions.entries
                    .firstOrNull { (_, cells) ->
                        cells.any { it.first == rowIndex && it.second == colIndex }
                    }?.key ?: 0

                CrownsCell(
                    state = cellState,
                    regionID = regionId
                )
            }
        }

        _uiState.value = CrownsUiState.Success(
            CrownsBoard(
                size = state.size,
                cells = cellsWithRegions,
                regions = state.regions
            )
        )
    }

    /**
     * Функция saveState сохраняет текущее состояние игры.
     */
    private fun saveState() {
        val state = _uiState.value as? CrownsUiState.Success ?: return

        viewModelScope.launch {
            repository.saveState(
                CrownsState(
                    size = state.board.size,
                    cells = state.board.cells.map { row -> row.map { it.state } },
                    regions = state.board.regions,
                    isGameCompleted = false,
                    elapsedTime = _elapsedTime.value
                )
            )
        }
    }

    /**
     * Функция generateNewBoard генерирует новое игровое поле.
     */
    private fun generateNewBoard() {
        timerJob?.cancel()
        _elapsedTime.value = 0

        incrementStartedGames()

        val size = _settings.value?.boardSize ?: 5
        viewModelScope.launch {
            val board = generator(size)
            _uiState.value = CrownsUiState.Success(board)
            startTimer()
            saveState()
        }
    }

    /**
     * Функция incrementStartedGames увеличивает число начатых игр
     * для экрана статистики.
     */
    private fun incrementStartedGames() {
        viewModelScope.launch {
            statsRepository.updateStats {
                copy(startedGames = startedGames + 1)
            }
        }
    }

    /**
     * Функция handleCellClick обрабатывает нажатие на ячейку поля Crowns.
     */
    fun handleCellClick(row: Int, col: Int) {
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.place)
        }

        val currentState = _uiState.value as? CrownsUiState.Success ?: return
        val cell = currentState.board.cells[row][col]
        val settings = _settings.value ?: return

        when (cell.state) {
            CellState.EMPTY -> {
                // Сначала ставим корону.
                val newCells = currentState.board.cells.map { it.toMutableList() }
                if (!settings.autoCrossEnabled) {
                    newCells[row][col] = cell.copy(state = CellState.CROSS)
                } else {
                    newCells[row][col] = cell.copy(state = CellState.CROWN)
                }
                _uiState.value = currentState.copy(board = currentState.board.copy(cells = newCells))

                // Затем добавляем крестики.
                if (settings.autoCrossEnabled) {
                    updateCrosses(row, col, isAdding = true)
                }
            }
            CellState.CROWN -> {
                // Сначала убираем корону.
                val newCells = currentState.board.cells.map { it.toMutableList() }
                newCells[row][col] = cell.copy(state = CellState.EMPTY)
                _uiState.value = currentState.copy(board = currentState.board.copy(cells = newCells))

                // Затем удаляем крестики.
                if (settings.autoCrossEnabled) {
                    updateCrosses(row, col, isAdding = false)
                }
            }
            CellState.CROSS -> {
                if (!settings.autoCrossEnabled) {
                    val newCells = currentState.board.cells.map { it.toMutableList() }
                    newCells[row][col] = cell.copy(state = CellState.CROWN)
                    _uiState.value = currentState.copy(board = currentState.board.copy(cells = newCells))
                }
            }
        }

        saveState()
        checkGameCompletion((_uiState.value as CrownsUiState.Success).board)
    }

    /**
     * Функция checkGameCompletion проверяет условие заверщения игры.
     */
    private fun checkGameCompletion(board: CrownsBoard) {
        if (validator(board)) {
            handleVictory()
        }
    }

    /**
     * Функция handleVictory обрабатывает завершение игры (победу).
     * Она сбрасывает прогресс и сохраняет результат в статистику.
     */
    private fun handleVictory() {
        viewModelScope.launch {
            val currentState = _uiState.value as? CrownsUiState.Success ?: return@launch
            val currentTime = _elapsedTime.value

            val currentScore = calculateCrownsScore(currentTime, currentState.board.size)

            statsRepository.updateStats {
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
                CrownsState(
                    size = currentState.board.size,
                    cells = currentState.board.cells.map { row -> row.map { it.state } },
                    regions = currentState.board.regions,
                    isGameCompleted = true,
                    elapsedTime = currentTime
                )
            )
            _uiState.value = CrownsUiState.Win(
                score = currentScore,
                elapsedTime = _elapsedTime.value
            )
        }
    }

    /**
     * Функция onResetClick обрабатывает нажатие на кнопку "Очистить поле".
     */
    fun onResetClick() {
        if (settings.value?.soundEnabled == true) {
            soundManager.playSound(R.raw.replay)
        }

        val currentState = _uiState.value as? CrownsUiState.Success ?: return

        val newCells = currentState.board.cells.map { row ->
            row.map { cell ->
                cell.copy(state = CellState.EMPTY)
            }
        }

        _uiState.value = currentState.copy(
            board = currentState.board.copy(cells = newCells)
        )

        saveState()
    }

    /**
     * Функция startTimer отвечает за запуск внутреигрового таймера.
     */
    fun startTimer(initialTime: Long = 0) {
        timerJob?.cancel()
        _elapsedTime.value = initialTime
        lastSaveTime = System.currentTimeMillis()

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _elapsedTime.value += 1000L
                saveTimeAuto()
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
     * Функция для автоматического сохранения таймера каждую секунду.
     */
    private fun saveTimeAuto() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastSaveTime > 1000) {
            saveState()
            lastSaveTime = currentTime
        }
    }

    /**
     * Функция calculateCrownsScore отвечает за вычисление итоговых очков режима Crowns.
     */
    private fun calculateCrownsScore(millis: Long, size: Int): Int {
        val seconds = ((millis / 1000) % 60).toInt()
        val score: Int = (1500 - seconds) * size
        return score
    }

    /**
     * Функция updateCrosses отвечает за авторасстановку крестиков на поле.
     * Если корона устанавливается, то устанавливаются крестики. Если корона
     * убирается, то крестики убираются.
     */
    private fun updateCrosses(row: Int, col: Int, isAdding: Boolean) {
        val currentState = _uiState.value as? CrownsUiState.Success ?: return
        val board = currentState.board
        val newCells = board.cells.map { row -> row.toMutableList() }.toMutableList()

        getAffectedCells(board, row, col).forEach { (x, y) ->
            val cell = newCells[x][y]
            when {
                isAdding && cell.state == CellState.EMPTY ->
                    newCells[x][y] = cell.copy(state = CellState.CROSS)

                !isAdding && cell.state == CellState.CROSS -> {
                    val shouldRemove = !isProtectedByOtherCrowns(
                        cells = newCells,
                        x = x,
                        y = y,
                        excludeRow = row,
                        excludeCol = col
                    )
                    if (shouldRemove) {
                        newCells[x][y] = cell.copy(state = CellState.EMPTY)
                    }
                }
            }
        }

        _uiState.value = currentState.copy(board = board.copy(cells = newCells))
    }

    /**
     * Функция getAffectedCells возвращает список ячеек, затронутых действием.
     * Действием может быть установка или снятие короны из ячейки.
     */
    private fun getAffectedCells(board: CrownsBoard, row: Int, col: Int): List<Pair<Int, Int>> {
        val size = board.size
        val cells = mutableListOf<Pair<Int, Int>>().apply {

            // Горизонталь и вертикаль.
            for (i in 0 until size) {
                if (i != col) add(row to i)
                if (i != row) add(i to col)
            }

            // Диагонали.
            val diag1 = (row - col).let { diff ->
                (0 until size).mapNotNull { x ->
                    val y = x - diff
                    if (y in 0 until size) x to y else null
                }
            }

            val diag2 = (row + col).let { sum ->
                (0 until size).mapNotNull { x ->
                    val y = sum - x
                    if (y in 0 until size) x to y else null
                }
            }

            addAll(diag1 + diag2)
        }

        return cells.distinct()
    }

    /**
     * Функция isProtectedByOtherCrowns проверяет, защищена ли ячейка другими коронами.
     */
    private fun isProtectedByOtherCrowns(
        cells: List<MutableList<CrownsCell>>,
        x: Int,
        y: Int,
        excludeRow: Int,
        excludeCol: Int
    ): Boolean {
        // Строка.
        if (cells[x].anyIndexed { colIdx, cell ->
                colIdx != excludeCol && cell.state == CellState.CROWN
            }) return true

        // Столбец.
        if (cells.anyIndexed { rowIdx, row ->
                rowIdx != excludeRow && row[y].state == CellState.CROWN
            }) return true

        // Диагонали.
        return checkDiagonals(cells, x, y, excludeRow, excludeCol)
    }

    /**
     * Вспомогательная функция для проверки элементов списка.
     * Используется для проверки строк или столбцов.
     */
    private inline fun <T> List<T>.anyIndexed(predicate: (Int, T) -> Boolean): Boolean {
        forEachIndexed { index, element ->
            if (predicate(index, element)) return true
        }
        return false
    }

    /**
     * Функция checkDiagonals проверяет диагонали на наличие конфликтов.
     */
    private fun checkDiagonals(
        cells: List<List<CrownsCell>>,
        x: Int,
        y: Int,
        excludeRow: Int,
        excludeCol: Int
    ): Boolean {
        val size = cells.size
        val diff = x - y
        val sum = x + y

        return (0 until size).any { i ->
            val j1 = i - diff
            val j2 = sum - i

            (j1 in 0 until size && cells[i][j1].state == CellState.CROWN && !(i == excludeRow && j1 == excludeCol)) ||
                    (j2 in 0 until size && cells[i][j2].state == CellState.CROWN && !(i == excludeRow && j2 == excludeCol))
        }
    }
}