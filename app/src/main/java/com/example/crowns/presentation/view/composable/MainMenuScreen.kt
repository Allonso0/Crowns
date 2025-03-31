package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R

@Composable
fun MainMenuScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (butCrowns,
            butKS,
            butStats,
            butCrownsSet,
            butKSSet,
            text,
            logo) = createRefs()

        Box(modifier = Modifier.background(gradient).fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                //.shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .shadow(elevation = 16.dp, shape = CircleShape)
                .background(color = colorResource(R.color.backgroundLight))
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(butCrowns.top, margin = 10.dp)
                    centerHorizontallyTo(parent)
                    centerHorizontallyTo(parent)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "Логотип",
                tint = colorResource(R.color.backgroundDark),
                modifier = Modifier.fillMaxSize(0.85f).offset(y = 12.dp)
            )
        }


        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.backgroundDark),
                containerColor = colorResource(R.color.backgroundLight)
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(70.dp)
                .constrainAs(butCrowns) {
                    absoluteLeft.linkTo(butStats.absoluteLeft)
                    centerVerticallyTo(parent)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("Crowns")
            }) {
            Text(
                text = "Crowns",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = colorResource(R.color.backgroundLight),
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .height(70.dp)
                .constrainAs(butCrownsSet) {
                    absoluteRight.linkTo(butStats.absoluteRight)
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
                .fillMaxWidth(0.6f)
                .height(70.dp)
                .constrainAs(butKS) {
                    top.linkTo(butCrowns.bottom, margin = 20.dp)
                    absoluteLeft.linkTo(butStats.absoluteLeft)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("KillerSudoku")
            }) {
            Text(
                text = "Killer Sudoku",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = colorResource(R.color.backgroundLight),
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .height(70.dp)
                .constrainAs(butKSSet) {
                    absoluteRight.linkTo(butStats.absoluteRight)
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
                .fillMaxWidth(0.8f)
                .height(70.dp)
                .constrainAs(butStats) {
                    top.linkTo(butKS.bottom, margin = 20.dp)
                    centerHorizontallyTo(parent)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                navController.navigate("Statistics")
            }) {
            Text(
                text = "Статистика",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = "Mobile app “Crowns” v 1.0",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.backgroundLight),
            modifier = Modifier.constrainAs(text) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )
    }
}