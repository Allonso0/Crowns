package com.example.crowns.presentation.view.composable

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.KillerSudokuVM
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.model.KillerSudokuBoard
import com.example.crowns.domain.model.KillerSudokuCell
import com.example.crowns.presentation.viewmodel.KillerSudokuUiState
/**
 * Composable-функция KillerSudokuScreen - это основной экран режима.
 * Управляет состоянием игры и отображает:
 * - Экран игры
 * - Экран загрузки или экран ошибки в зависимости от состояния.
 */
@Composable
fun KillerSudokuScreen(
    navController: NavController,
    vm: KillerSudokuVM = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val settings by vm.settings.collectAsState()

    val elapsedTime by vm.elapsedTime.collectAsState()
    val formattedTime = remember(elapsedTime) { timerFormat(elapsedTime) }

    LaunchedEffect(Unit) {
        vm.loadSavedState()
    }

    DisposableEffect(Unit) {
        vm.startTimer()
        onDispose { vm.stopTimer() }
    }

    // Обрабатываем состояние UI.
    when (val state = uiState) {
        is KillerSudokuUiState.Loading -> FullScreenLoader()
        is KillerSudokuUiState.Success -> GameContent(state, vm, navController, formattedTime, settings ?: KillerSudokuSettings())
        is KillerSudokuUiState.Error -> ErrorScreen(
            message = state.message,
            onRetry = { vm.loadNewGame() }
        )
        is KillerSudokuUiState.Win -> {
            navController.navigate("WinScreenKS?score=${state.score}&time=${state.elapsedTime}")
            navController.popBackStack("KillerSudoku", inclusive = true)
        }
        is KillerSudokuUiState.Lose -> {
            navController.navigate("LoseScreenKS") {
                popUpTo("KillerSudoku") { inclusive = true }
            }
        }
    }
}

/**
 * Composable-функция KillerSudokuScreen отображает
 * основной экран игры:
 * - Градиентный фон
 * - Верхний тулбар с кнопками
 * - Игровое поле
 * - Цифровую клавиатуру
 */
@Composable
private fun GameContent(
    state: KillerSudokuUiState.Success,
    viewModel: KillerSudokuVM,
    navController: NavController,
    timerString: String,
    settings: KillerSudokuSettings
) {
    // Градиент для фона.
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (upperBar,
            butHome,
            butRules,
            gameField,
            upperNumPad,
            downNumPad,
            actionButtons,
            score,
            bottomBar,
            errorCount,
            timer) = createRefs()

        // Фон.
        Box(modifier = Modifier.background(gradient).fillMaxSize())

        // Верхний тулбар.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.12f)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(color = Color.White)
                .constrainAs(upperBar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Счет: ${state.score}",
                    color = colorResource(R.color.backgroundDark),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        settings.let { safeSettings ->
            if (safeSettings.showTimer) {
                Text(
                    text = timerString,
                    modifier = Modifier
                        .constrainAs(timer) {
                            centerHorizontallyTo(parent)
                            top.linkTo(downNumPad.bottom, margin = 16.dp)
                        },
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.backgroundLight)
                )
            }
        }

        // Лимит ошибок (если включен в настройках)
        settings.let { safeSettings ->
            if (safeSettings.errorLimitEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(color = Color.White)
                        .constrainAs(bottomBar) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                )

                // Отображение текущего количества ошибок
                Text(
                    text = "Ошибки: ${state.errorCount}/3",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.backgroundDark),
                    modifier = Modifier
                        .constrainAs(errorCount) {
                            centerHorizontallyTo(parent)
                            centerVerticallyTo(bottomBar)
                        }
                )
            }
        }

        // Кнопка для вовзращения в главное меню.
        FloatingActionButton(
            contentColor = Color.White,
            containerColor = colorResource(R.color.backgroundDark),
            modifier = Modifier
                .size(65.dp)
                .constrainAs(butHome) {
                    absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
                    bottom.linkTo(upperBar.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                viewModel.onExit()
                navController.popBackStack()
            }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "В главное меню",
            )
        }

        // Кнопка для перехода на экран с правилами.
        FloatingActionButton(
            contentColor = Color.White,
            containerColor = colorResource(R.color.backgroundDark),
            modifier = Modifier
                .size(65.dp)
                .constrainAs(butRules) {
                    absoluteRight.linkTo(parent.absoluteRight, margin = 20.dp)
                    bottom.linkTo(upperBar.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                viewModel.onExit()
                navController.navigate("KillerSudokuRules") {
                    popUpTo("KillerSudoku") { saveState = true }
                }
            }) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Как играть?",
            )
        }

        // Игровое поле.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(25.dp)
                .constrainAs(gameField) {
                    top.linkTo(upperBar.bottom)
                    centerHorizontallyTo(parent)
                    width = Dimension.fillToConstraints
                    height = Dimension.ratio("1:1")
                }
        ) {
            SudokuField(
                board = state.board,
                selectedCell = state.selectedCell,
                onCellClick = viewModel::onCellSelected,
                viewModel = viewModel,
                settings = settings
            )
        }

        // Верхняя цифровая клавиатура (1-5).
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(upperNumPad) {
                    top.linkTo(actionButtons.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            (1..5).forEach { number ->
                NumberButton(
                    number = number,
                    onClick = { viewModel.onNumberInput(number) }
                )
            }
        }

        // Нижняя цифровая клавиатура (6-9).
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(downNumPad) {
                    top.linkTo(upperNumPad.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            (6..9).forEach { number ->
                NumberButton(
                    number = number,
                    onClick = { viewModel.onNumberInput(number) }
                )
            }
        }

        // Ряд с кнопками действий.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(actionButtons) {
                    top.linkTo(gameField.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            EraserButton(onClick = viewModel::onEraseClick)

            ClearButton(onClick = viewModel::onClearClick)

            HintButton(onClick = viewModel::onHintClick)
        }
    }
}

/**
 * Composable-функция SudokuField рисует игровое поле.
 */
@Composable
private fun SudokuField(
    board: KillerSudokuBoard,
    selectedCell: Pair<Int, Int>?,
    onCellClick: (Int, Int) -> Unit,
    viewModel: KillerSudokuVM,
    settings: KillerSudokuSettings
) {
    // Настройки стилей
    val cageBorderColor = Color.Black
    val highlightColor = Color.Blue.copy(alpha = 0.1f)
    val selectedValue by viewModel.selectedCellValue.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellSize = size.width / 9f
                    val row = (offset.y / cellSize).toInt().coerceIn(0..8)
                    val col = (offset.x / cellSize).toInt().coerceIn(0..8)
                    onCellClick(row, col)
                }
            }) {
            val cellSize = size.width / 9f

            // Подсветка строки и столбца выбранной ячейки
            selectedCell?.let { (x, y) ->
                drawRect(
                    color = highlightColor,
                    topLeft = Offset(y * cellSize, 0f),
                    size = Size(cellSize, size.height)
                )
                drawRect(
                    color = highlightColor,
                    topLeft = Offset(0f, x * cellSize),
                    size = Size(size.width, cellSize)
                )
            }

            // Отрисовка границ блоков 3x3.
            drawGrid(cellSize)

            // Отрисовка клеток (cages) и их сумм.
            board.cages.forEach { (_, cells) ->
                drawCage(cells, cellSize, cageBorderColor, board)
            }

            // Отрисовка значений ячеек.
            drawCellValues(board, cellSize, selectedValue, settings)
        }
    }
}

/**
 * Функция drawGrid отрисовывает сетку 9x9 и жирные линии блоков 3x3.
 */
private fun DrawScope.drawGrid(cellSize: Float) {
    // Тонкие линии для ячеек.
    for (i in 0..9) {
        drawLine(
            color = Color.Black.copy(alpha = 0.3f),
            start = Offset(i * cellSize, 0f),
            end = Offset(i * cellSize, size.height),
            strokeWidth = 1.dp.toPx()
        )
        drawLine(
            color = Color.Black.copy(alpha = 0.3f),
            start = Offset(0f, i * cellSize),
            end = Offset(size.width, i * cellSize),
            strokeWidth = 1.dp.toPx()
        )
    }

    // Жирные линии для блоков 3x3.
    for (i in 0..3) {
        drawLine(
            color = Color.Black,
            start = Offset(i * 3 * cellSize, 0f),
            end = Offset(i * 3 * cellSize, size.height),
            strokeWidth = 3.dp.toPx()
        )
        drawLine(
            color = Color.Black,
            start = Offset(0f, i * 3 * cellSize),
            end = Offset(size.width, i * 3 * cellSize),
            strokeWidth = 3.dp.toPx()
        )
    }
}

/**
 * Функция drawCage отрисовывает границы клетки и её сумму.
 */
private fun DrawScope.drawCage(
    cells: List<Pair<Int, Int>>,
    cellSize: Float,
    borderColor: Color,
    board: KillerSudokuBoard
) {
    val path = Path()
    val textPaint = Paint().apply {
        textSize = 10.sp.toPx()
        color = android.graphics.Color.parseColor("#222222")
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    val padding = 4.dp.toPx() // Отступ пунктира от границы клетки.

    cells.forEach { (row, col) ->
        // Верхняя граница.
        if (!cells.contains(row - 1 to col)) {
            path.moveTo(col * cellSize + padding, row * cellSize + padding)
            path.lineTo((col + 1) * cellSize - padding, row * cellSize + padding)
        }
        // Нижняя граница.
        if (!cells.contains(row + 1 to col)) {
            path.moveTo(col * cellSize + padding, (row + 1) * cellSize - padding)
            path.lineTo((col + 1) * cellSize - padding, (row + 1) * cellSize - padding)
        }
        // Левая граница.
        if (!cells.contains(row to col - 1)) {
            path.moveTo(col * cellSize + padding, row * cellSize + padding)
            path.lineTo(col * cellSize + padding, (row + 1) * cellSize - padding)
        }
        // Правая граница.
        if (!cells.contains(row to col + 1)) {
            path.moveTo((col + 1) * cellSize - padding, row * cellSize + padding)
            path.lineTo((col + 1) * cellSize - padding, (row + 1) * cellSize - padding)
        }
    }

    // Рисуем пунктирные границы.
    drawPath(
        path = path,
        color = borderColor,
        style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f))
        )
    )

    // Рисуем сумму клетки в левом верхнем углу.
    val minCell = cells.minBy { it.first * 9 + it.second }
    val sum = board.cells[minCell.first][minCell.second].cageSum.toString()
    drawContext.canvas.nativeCanvas.drawText(
        sum,
        minCell.second * cellSize + 5.dp.toPx(),
        minCell.first * cellSize + 13.dp.toPx(),
        textPaint
    )
}

/**
 * Функция drawCellValues отрисовывает цифры в ячейках.
 */
private fun DrawScope.drawCellValues(
    board: KillerSudokuBoard,
    cellSize: Float,
    selectedValue: Int?,
    settings: KillerSudokuSettings
) {
    val fixedPaint = Paint().apply {
        textSize = 18.sp.toPx()
        color = android.graphics.Color.BLACK
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val correctPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#003494")
        textSize = 18.sp.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val errorPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#B20000")
        textSize = 18.sp.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    val hintPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#006600")
        textSize = 18.sp.toPx()
        textAlign = android.graphics.Paint.Align.CENTER
    }

    board.cells.forEachIndexed { x, row ->
        row.forEachIndexed { y, cell ->
            cell.value?.let { value ->
                val paint = when {
                    cell.isFixed -> fixedPaint
                    (cell.isError && settings.highlightErrors) -> errorPaint
                    cell.isHint -> hintPaint
                    else -> correctPaint
                }

                // Рисуем фон для подсвеченных ячеек, если
                // включена соответствующая опция.
                if (settings.highlightSameNumbers) {
                    val isHighlighted = (value == selectedValue) && (selectedValue != null)
                    if (isHighlighted) {
                        drawRect(
                            color = Color.Blue.copy(alpha = 0.2f),
                            topLeft = Offset(y * cellSize, x * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }

                drawContext.canvas.nativeCanvas.drawText(
                    value.toString(),
                    y * cellSize + cellSize / 2,
                    x * cellSize + cellSize / 2 + paint.textSize / 3,
                    paint
                )
            }
        }
    }
}

/**
 * Кнопка для ввода цифры.
 */
@Composable
private fun NumberButton(
    number: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.size(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.backgroundLight),
            contentColor = colorResource(R.color.backgroundDark)
        )
    ) {
        Text(
            text = number.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Экран ошибки.
 * Простенький, потому что не должен появляться :)
 */
@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Повторить", color = Color.Black)
            }
        }
    }
}

/**
 * Экран загрузки.
 * Простенький, потому что не должен появляться :)
 */
@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Загрузка...",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Кнопка для удаления введеной цифры.
 */
@Composable
private fun EraserButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(55.dp),
        containerColor = colorResource(R.color.backgroundLight),
        contentColor = colorResource(R.color.backgroundDark)
    ) {
        Icon(
            painter = painterResource(R.drawable.eraser),
            contentDescription = "Ластик",
            modifier = Modifier.size(40.dp)
        )
    }
}

/**
 * Кнопка для очистки всего поля.
 */
@Composable
private fun ClearButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(55.dp),
        containerColor = colorResource(R.color.backgroundLight),
        contentColor = colorResource(R.color.backgroundDark)
    ) {
        Icon(
            painter = painterResource(R.drawable.delete),
            contentDescription = "Очистить всё",
            modifier = Modifier.size(40.dp)
        )
    }
}

/**
 * Кнопка подсказки.
 */
@Composable
private fun HintButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(55.dp),
        containerColor = colorResource(R.color.backgroundLight),
        contentColor = colorResource(R.color.backgroundDark)
    ) {
        Icon(
            painter = painterResource(R.drawable.idea),
            contentDescription = "Подсказка",
            modifier = Modifier.size(40.dp)
        )
    }
}

// Функция для форматирования таймера.
private fun timerFormat(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / 60000) % 60
    return "%02d:%02d".format(minutes, seconds)
}