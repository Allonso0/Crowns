package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.MainMenuVM

@Composable
fun MainMenuScreen(navController: NavController, vm: MainMenuVM = viewModel()) {
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (butCrowns,
            butKS,
            butQueens,
            butStats,
            butCrownsSet,
            butKSSet,
            butQueensSet,
            text,
            logo) = createRefs()

        Box(modifier = Modifier.background(gradient).fillMaxSize())

        Box(
            modifier = Modifier
                .size(280.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(color = colorResource(R.color.backgroundLight))
                .constrainAs(logo) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(butCrowns.top, margin = 60.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Ведутся работы над логотипом",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.backgroundDark),
                textAlign = TextAlign.Center
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundDark),
                containerColor = colorResource(R.color.backgroundLight)
            ),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .constrainAs(butCrowns) {
                    absoluteLeft.linkTo(logo.absoluteLeft)
                    centerVerticallyTo(parent)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("Crowns")
            }) {
            Text(
                text = "Crowns",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = colorResource(R.color.backgroundLight),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .constrainAs(butCrownsSet) {
                    absoluteLeft.linkTo(butCrowns.absoluteRight, margin = 20.dp)
                    centerVerticallyTo(parent)
                },
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("CrownsSettings")
            }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Настройки",
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundDark),
                containerColor = colorResource(R.color.backgroundLight)
            ),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .constrainAs(butKS) {
                    top.linkTo(butCrowns.bottom, margin = 20.dp)
                    absoluteLeft.linkTo(logo.absoluteLeft)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("KillerSudoku")
            }) {
            Text(
                text = "Killer Sudoku",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = colorResource(R.color.backgroundLight),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .constrainAs(butKSSet) {
                    absoluteLeft.linkTo(butKS.absoluteRight, margin = 20.dp)
                    top.linkTo(butCrownsSet.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("KillerSudokuSettings")
            }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Настройки",
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundDark),
                containerColor = colorResource(R.color.backgroundLight)
            ),
            modifier = Modifier
                .width(200.dp)
                .height(60.dp)
                .constrainAs(butQueens) {
                    top.linkTo(butKS.bottom, margin = 20.dp)
                    absoluteLeft.linkTo(logo.absoluteLeft)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("NQueens")
            }) {
            Text(
                text = "N-Queens",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = colorResource(R.color.backgroundLight),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .constrainAs(butQueensSet) {
                    absoluteLeft.linkTo(butQueens.absoluteRight, margin = 20.dp)
                    top.linkTo(butKSSet.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("NQueensSettings")
            }) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Настройки",
            )
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundDark),
                containerColor = colorResource(R.color.backgroundLight)
            ),
            modifier = Modifier
                .width(280.dp)
                .height(60.dp)
                .constrainAs(butStats) {
                    top.linkTo(butQueens.bottom, margin = 20.dp)
                    centerHorizontallyTo(parent)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("Statistics")
            }) {
            Text(
                text = "Статистика",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = "Mobile app “Crowns” v 1.0",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.backgroundLight),
            modifier = Modifier.constrainAs(text) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )
    }
}