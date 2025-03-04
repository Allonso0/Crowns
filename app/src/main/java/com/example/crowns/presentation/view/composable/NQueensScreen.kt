package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.NQueensVM

@Composable
fun NQueensScreen(navController: NavController, vm: NQueensVM = viewModel()) {
    val context = LocalContext.current

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
            textScore,
            textTimer,
            butReplay,
            butTheme,
            butHint) = createRefs()

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
                }
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = colorResource(R.color.backgroundDark)
            ),
            modifier = Modifier
                .size(40.dp)
                .constrainAs(butHome) {
                    absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navController.popBackStack()
            }) {

        }

        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = colorResource(R.color.backgroundDark)
            ),
            modifier = Modifier
                .size(40.dp)
                .constrainAs(butRules) {
                    absoluteRight.linkTo(parent.absoluteRight, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                },
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp),
            onClick = {
                navController.navigate("NQueensRules")
            }) {
            Icon(
                Icons.Filled.Home,
                contentDescription = "Как играть?",
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
        }
    }
}