package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.crowns.presentation.viewmodel.CrownsVM

@Composable
fun CrownsScreen(navController: NavController, vm: CrownsVM = viewModel()) {
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (toolBar,
            butHome,
            butRules,
            gameField,
            butReplay,
            butTheme,
            butHint,
            timer) = createRefs()

        Box(modifier = Modifier.background(gradient).fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(color = Color.White)
                .constrainAs(toolBar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${vm.score.toInt()}",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        FloatingActionButton(
            contentColor = Color.White,
            containerColor = colorResource(R.color.backgroundDark),
            modifier = Modifier
                .size(40.dp)
                .constrainAs(butHome) {
                    absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navController.popBackStack()
            }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "В главное меню",
            )
        }

        FloatingActionButton(
            contentColor = Color.White,
            containerColor = colorResource(R.color.backgroundDark),
            modifier = Modifier
                .size(40.dp)
                .constrainAs(butRules) {
                    absoluteRight.linkTo(parent.absoluteRight, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navController.navigate("CrownsRules")
            }) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "Как играть?",
            )
        }

        Box(
            modifier = Modifier
                .size(350.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .constrainAs(gameField) {
                    centerHorizontallyTo(parent)
                    top.linkTo(toolBar.bottom, margin = 100.dp)
                },
        )

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = Color.White,
            modifier = Modifier
                .size(60.dp)
                .constrainAs(butReplay) {
                    absoluteLeft.linkTo(gameField.absoluteLeft, margin = 20.dp)
                    top.linkTo(gameField.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {

            }) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "В главное меню",
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = Color.White,
            modifier = Modifier
                .size(60.dp)
                .constrainAs(butHint) {
                    absoluteRight.linkTo(gameField.absoluteRight, margin = 20.dp)
                    top.linkTo(gameField.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {

            }) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "В главное меню",
            )
        }

        FloatingActionButton(
            contentColor = colorResource(R.color.backgroundDark),
            containerColor = Color.White,
            modifier = Modifier
                .size(60.dp)
                .constrainAs(butTheme) {
                    centerHorizontallyTo(parent)
                    top.linkTo(gameField.bottom, margin = 20.dp)
                },
            shape = RoundedCornerShape(12.dp),
            onClick = {

            }) {
            Icon(
                Icons.Filled.Create,
                contentDescription = "В главное меню",
            )
        }

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .constrainAs(timer) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(gameField.top, margin = 20.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${vm.timerMinutes.toInt()}:${vm.timerSeconds.toInt()}",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    }
}