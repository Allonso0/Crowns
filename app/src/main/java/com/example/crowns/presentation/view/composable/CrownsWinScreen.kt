package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.crowns.R

@Composable
fun CrownsWinScreen(
    score: Int,
    time: Long,
    onMenu: () -> Unit
) {
    // Градиент для фона.
    val gradient = Brush.verticalGradient(
        0.0f to Color.White,
        1.0f to colorResource(R.color.backgroundDark),
        startY = 0.0f,
        endY = 1700.0f
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
    ) {
        val (title, message, button) = createRefs()

        val (cr1, cr2, cr3, cr4, cr5, cr6, cr7, cr8) = createRefs()

        Button(
            onClick = onMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = colorResource(R.color.backgroundDark)
            ),
            modifier = Modifier
                .width(160.dp)
                .height(40.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .constrainAs(button) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, margin = 50.dp)
                }
        ) {
            Text(
                text = "В меню",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(170.dp)
                .rotate(25f)
                .blur(8.dp)
                .constrainAs(cr1) {
                    end.linkTo(parent.absoluteRight, margin = 30.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(90.dp)
                .rotate(-25f)
                .constrainAs(cr2) {
                    start.linkTo(parent.absoluteLeft, margin = 30.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(60.dp)
                .rotate(-14f)
                .blur(3.dp)
                .constrainAs(cr3) {
                    start.linkTo(parent.absoluteLeft, margin = 100.dp)
                    top.linkTo(parent.top, margin = 150.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(60.dp)
                .rotate(45f)
                .constrainAs(cr4) {
                    end.linkTo(parent.absoluteRight, margin = 20.dp)
                    centerVerticallyTo(message)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(40.dp)
                .rotate(-25f)
                .blur(4.dp)
                .constrainAs(cr5) {
                    start.linkTo(parent.absoluteLeft, margin = 20.dp)
                    top.linkTo(parent.top, margin = 350.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(250.dp)
                .rotate(-25f)
                .blur(2.dp)
                .constrainAs(cr6) {
                    start.linkTo(parent.absoluteLeft, margin = 20.dp)
                    top.linkTo(parent.top, margin = 450.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(40.dp)
                .rotate(25f)
                .constrainAs(cr7) {
                    end.linkTo(parent.absoluteRight, margin = 40.dp)
                    top.linkTo(parent.top, margin = 485.dp)
                }
        )

        Icon(
            painter = painterResource(R.drawable.crown),
            tint = colorResource(R.color.backgroundDark),
            contentDescription = "Корона",
            modifier = Modifier
                .size(40.dp)
                .rotate(15f)
                .blur(1.dp)
                .constrainAs(cr8) {
                    end.linkTo(parent.absoluteRight, margin = 125.dp)
                    top.linkTo(parent.top, margin = 355.dp)
                }
        )

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(100.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .border(
                    width = 5.dp,
                    color = colorResource(R.color.backgroundDark),
                    shape = RoundedCornerShape(20.dp)
                )
                .constrainAs(title) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(message.top, margin = 50.dp)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "ПОБЕДА!",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .width(220.dp)
                .height(90.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = Color(255f, 255f, 255f, 0.35f),
                    shape = RoundedCornerShape(12.dp)
                )
                .constrainAs(message) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Счёт:",
                        color = colorResource(R.color.backgroundDark),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = score.toString(),
                        color = colorResource(R.color.backgroundDark),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .width(210.dp)
                        .padding(start = 10.dp)
                        .background(Color.Gray)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Время:",
                        color = colorResource(R.color.backgroundDark),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = time.timeFormat(),
                        color = colorResource(R.color.backgroundDark),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        }
    }
}