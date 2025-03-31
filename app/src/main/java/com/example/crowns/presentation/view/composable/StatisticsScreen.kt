package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.StatisticsVM

@Composable
fun StatisticsScreen(navController: NavController) {
    val statsVM: StatisticsVM = hiltViewModel()
    val statsKillerSudoku by statsVM.statsKS.collectAsState()
    val statsCrowns by statsVM.statsCrowns.collectAsState()


    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.secondGradientColor),
        1.0f to Color.White,
        startY = 0.0f,
        endY = 5000.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (
            title,
            butHome,
            lazyCol
        ) = createRefs()

        Box(modifier = Modifier.background(gradient).fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(color = Color.White)
                .constrainAs(title) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Статистика",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = colorResource(R.color.backgroundDark)
            ),
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
                .constrainAs(butHome) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 40.dp)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Text(
                text = "Назад",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier
                .width(320.dp)
                .height(600.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .constrainAs(lazyCol) {
                    centerHorizontallyTo(parent)
                    top.linkTo(title.bottom, margin = 20.dp)
                    bottom.linkTo(butHome.top, margin = 20.dp)
                },
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item {
                Text(
                    text = "Игры",
                    color = colorResource(R.color.backgroundDark),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Начатых игр (всего)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = (statsCrowns.startedGames + statsKillerSudoku.startedGames).toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Начатых игр (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsCrowns.startedGames.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Начатых игр (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsKillerSudoku.startedGames.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Побед (всего)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = (statsCrowns.wins + statsKillerSudoku.wins).toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Побед (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsCrowns.wins.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Побед (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsKillerSudoku.wins.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Text(
                    text = "Счёт",
                    color = colorResource(R.color.backgroundDark),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Лучший счёт (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsCrowns.bestScore.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Лучший счёт (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsKillerSudoku.bestScore.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Text(
                    text = "Время",
                    color = colorResource(R.color.backgroundDark),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Лучшее время (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsCrowns.bestTime.timeFormat(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(20.dp))
                        .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Лучшее время (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = statsKillerSudoku.bestTime.timeFormat(),
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    }
}

fun Long.timeFormat(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}