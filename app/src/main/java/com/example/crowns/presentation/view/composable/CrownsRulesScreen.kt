package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R

@Composable
fun CrownsRulesScreen(navController: NavController) {
    val gradient = Brush.verticalGradient(
        0.0f to colorResource(R.color.secondGradientColor),
        1.0f to Color.White,
        startY = 0.0f,
        endY = 5000.0f
    )

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (
            toolbar,
            button,
            window,
            image
        ) = createRefs()

        createVerticalChain(image, window, chainStyle = ChainStyle.Packed)

        Box(modifier = Modifier.background(gradient).fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                .background(color = Color.White)
                .constrainAs(toolbar) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Как играть?",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Box(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 20.dp, end = 20.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .constrainAs(window) {
                    centerHorizontallyTo(parent)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "1. Ваша задача расставить короны так, чтобы в каждой строке, в каждом столбце, на каждой диагонали и в каждом цветовом регионе было по одной фигуре.\n" +
                        "\n" +
                        "2. Нажмите один раз, чтобы установить крестик в ячейку и дважды, чтобы установить в ячейку корону. Используйте крестики, чтобы обозначить ячейки, куда нельзя поставить корону.\n",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(20.dp)
            )
        }

        Box (
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .constrainAs(image) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.hamster),
                contentDescription = "Хомяк",
                modifier = Modifier.size(300.dp)
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
                .constrainAs(button) {
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
                text = "Ясно",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}