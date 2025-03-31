package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.data.database.entity.CrownsSettings
import com.example.crowns.domain.model.CellState
import com.example.crowns.domain.model.CrownsBoard
import com.example.crowns.presentation.viewmodel.CrownsUiState
import com.example.crowns.presentation.viewmodel.CrownsVM

@Composable
fun CrownsScreen(
    navController: NavController,
    vm: CrownsVM = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val settings by vm.settings.collectAsState()

    val elapsedTime by vm.elapsedTime.collectAsState()
    val formattedTime = remember(elapsedTime) { timerFormat(elapsedTime) }

    LaunchedEffect(Unit) {
        vm.loadSaveState()
    }

    DisposableEffect(Unit) {
        vm.startTimer()
        onDispose { vm.stopTimer() }
    }

    when(val state = uiState) {
        is CrownsUiState.Success -> {
            CrownsBoardComposable(state, vm, navController, formattedTime, settings ?: CrownsSettings())
        }
        is CrownsUiState.Loading -> FullScreenLoader()
        is CrownsUiState.Error -> ErrorScreen(state.message) { }
        is CrownsUiState.Win -> {
            navController.navigate("WinScreenCrowns?score=${state.score}&time=${state.elapsedTime}")
            navController.popBackStack("Crowns", inclusive = true)
        }
    }
}

@Composable
fun CrownsBoardComposable(
    state: CrownsUiState.Success,
    viewModel: CrownsVM,
    navController: NavController,
    timerString: String,
    settings: CrownsSettings
) {
    // Получаем размер поля
    val boardSize = state.board.size
    val cellSpacing = 1.dp

    // Градиент для фона.
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    // Диалоговое окно для начала новой игры.
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(
                text = "Новая игра",
                fontWeight = FontWeight.Bold
            ) },
            text = { Text("Вы хотите начать новый уровень?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onNewLevelClick()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.backgroundDark),
                        contentColor = Color.White
                    )
                ) { Text("Да") }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.backgroundDark),
                        contentColor = Color.White
                    )
                ) { Text("Нет") }
            },
            containerColor = Color.White,
            titleContentColor = colorResource(R.color.backgroundDark),
            textContentColor = colorResource(R.color.backgroundDark)
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        val (field,
            upperToolbar,
            bottomToolbar,
            butHome,
            butRules,
            buttonsKeyboard) = createRefs()

        // Верхний тулбар.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(color = Color.White)
                .constrainAs(upperToolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Crowns",
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.backgroundDark)
            )
        }

        // Нижний тулбар (таймер).
        settings.let { safeSettings ->
            if (safeSettings.showTimer) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                        .background(color = Color.White)
                        .constrainAs(bottomToolbar) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_timer),
                            contentDescription = "Restart",
                            modifier = Modifier.size(30.dp),
                            tint = colorResource(R.color.backgroundDark)
                        )

                        Spacer(modifier = Modifier.size(20.dp))

                        Text(
                            text = timerString,
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(R.color.backgroundDark)
                        )
                    }
                }
            }
        }


        // Внешний контейнер с отступами и центрированием.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .constrainAs(field) {
                    top.linkTo(upperToolbar.bottom)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            // Контейнер, который знает размер экрана.
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                val cellSize = (maxWidth - cellSpacing * (boardSize - 1)) / boardSize

                Column(
                    verticalArrangement = Arrangement.spacedBy(cellSpacing)
                ) {
                    for (i in 0 until boardSize) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(cellSpacing)
                        ) {
                            for (j in 0 until boardSize) {
                                val cell = state.board.cells[i][j]
                                Box(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .background(getColorForRegion(cell.regionID))
                                        .border(1.dp, Color.Transparent)
                                        .clickable { viewModel.handleCellClick(i, j) }
                                ) {
                                    when (cell.state) {
                                        CellState.EMPTY -> {}
                                        CellState.CROWN -> Icon(
                                            painter = painterResource(R.drawable.ic_crown),
                                            contentDescription = "Crown"
                                        )
                                        CellState.CROSS -> Icon(
                                            painter = painterResource(R.drawable.ic_cross),
                                            contentDescription = "Cross"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttonsKeyboard) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 120.dp)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = { viewModel.onResetClick() },
                modifier = Modifier
                    .width(180.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                containerColor = colorResource(R.color.backgroundLight),
                contentColor = colorResource(R.color.backgroundDark)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Очистить поле",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = "Restart",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            FloatingActionButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .width(180.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                containerColor = colorResource(R.color.backgroundLight),
                contentColor = colorResource(R.color.backgroundDark)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Новая игра",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Icon(
                        painter = painterResource(R.drawable.replay),
                        contentDescription = "New game",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Кнопка для вовзращения в главное меню.
        FloatingActionButton(
            contentColor = Color.White,
            containerColor = colorResource(R.color.backgroundDark),
            modifier = Modifier
                .size(50.dp)
                .constrainAs(butHome) {
                    absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                //viewModel.onExit()
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
                .size(50.dp)
                .constrainAs(butRules) {
                    absoluteRight.linkTo(parent.absoluteRight, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                //viewModel.onExit()
                navController.navigate("CrownsRules") {
                    popUpTo("Crowns") { saveState = true }
                }
            }) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Как играть?",
            )
        }
    }
}


@Composable
fun getColorForRegion(regionId: Int): Color {
    val colors = listOf(
        Color(0xFFBBDEFB),
        Color(0xFFC8E6C9),
        Color(0xFFFFF9C4),
        Color(0xFFFF6464),
        Color(0xFFD1C4E9),
        Color(0xFFFFCDD2),
        Color(0xFF56EFBF),
        Color(0xFFD0D0D0),
        Color(0xFFB069FF),
        Color(0xFFC7805E),
        Color(0xFF7966FA),
        Color(0xFF89EC74),
    )
    return colors[regionId % colors.size]
}

// Функция для форматирования таймера.
private fun timerFormat(millis: Long): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / 60000) % 60
    return "%02d:%02d".format(minutes, seconds)
}