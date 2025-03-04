package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.StatisticsVM

@Composable
fun StatisticsScreen(navController: NavController, vm: StatisticsVM = viewModel()) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Начатых игр (всего)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Начатых игр (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Начатых игр (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Начатых игр (N-Queens)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Побед (всего)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Побед (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Побед (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Побед (N-Queens)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучший счёт (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучший счёт (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучший счёт (N-Queens)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучшее время (Crowns)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучшее время (Killer Sudoku)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = colorResource(R.color.backgroundDark), shape = RoundedCornerShape(30.dp))
                        .background(color = Color.White)
                ) {
                    Text(
                        text = "Лучшее время (N-Queens)",
                        color = colorResource(R.color.backgroundDark),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 10.dp, top = 14.dp)
                    )
                }
            }
        }
    }
}