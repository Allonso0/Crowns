package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crowns.R

@Composable
fun KillerSudokuWinScreen(
    score: Int,
    onMenu: () -> Unit
) {
    // Градиент для фона.
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.backgroundLight),
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1500.0f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "ПОБЕДА!",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp
            )

            Text(
                text = "Счёт: $score",
                color = colorResource(R.color.backgroundLight),
                fontSize = 24.sp
            )

            Button(
                onClick = onMenu,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.backgroundLight),
                    contentColor = colorResource(R.color.backgroundDark)
                )
            ) {
                Text("В меню")
            }
        }
    }
}