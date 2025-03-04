package com.example.crowns.presentation.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.crowns.R
import kotlin.math.roundToInt

@Composable
fun NQueensSettingsScreen(navController: NavController) {
    val context = LocalContext.current

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
            box1,
            box2,
            box3,
            box4,
            switch1,
            switch2,
            switch3,
            slider,
            explain1,
            explain2,
            explain3
        ) = createRefs()

        var sliderPosition by remember { mutableFloatStateOf(4f) }

        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }

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
                text = "Настройки",
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

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(98.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box1) {
                    centerHorizontallyTo(parent)
                    top.linkTo(title.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Размер игрового поля: ${sliderPosition.roundToInt()}",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(slider) {
                    bottom.linkTo(box1.bottom, margin = 15.dp)
                    centerHorizontallyTo(box1)
                }
        ) {
            Slider(
                value = sliderPosition,
                valueRange = 4f..100f,
                steps = 95,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = colorResource(R.color.backgroundDark),
                    activeTrackColor = colorResource(R.color.backgroundDark),
                    inactiveTrackColor = colorResource(R.color.backgroundLight),
                    inactiveTickColor = colorResource(R.color.backgroundLight),
                    activeTickColor = colorResource(R.color.backgroundDark)
                )
            )
        }

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box2) {
                    centerHorizontallyTo(parent)
                    top.linkTo(box1.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Вкл/откл отобр. кнопки +/-",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(explain1) {
                    absoluteLeft.linkTo(box2.absoluteLeft, margin = 15.dp)
                    top.linkTo(box2.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Опция, отвечающая за отображение кнопки изменения масштаба",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box3) {
                    centerHorizontallyTo(parent)
                    top.linkTo(explain1.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Автопроверка ошибок",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(explain2) {
                    absoluteLeft.linkTo(box3.absoluteLeft, margin = 15.dp)
                    top.linkTo(box3.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Отмечать ячейки с ферзями, которые не соответствуют правилам " +
                        "расстановки",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box4) {
                    centerHorizontallyTo(parent)
                    top.linkTo(explain2.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Выделять области",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(explain3) {
                    absoluteLeft.linkTo(box4.absoluteLeft, margin = 15.dp)
                    top.linkTo(box4.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Выделять клетки, которые “бьёт” устанавливаемый ферзь",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Switch(
            checked = checkedState1.value,
            onCheckedChange = {
                checkedState1.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch1) {
                centerVerticallyTo(box2)
                absoluteRight.linkTo(box2.absoluteRight, margin = 15.dp)
            }
        )

        Switch(
            checked = checkedState2.value,
            onCheckedChange = {
                checkedState2.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch2) {
                centerVerticallyTo(box3)
                absoluteRight.linkTo(box3.absoluteRight, margin = 15.dp)
            }
        )

        Switch(
            checked = checkedState3.value,
            onCheckedChange = {
                checkedState3.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch3) {
                centerVerticallyTo(box4)
                absoluteRight.linkTo(box4.absoluteRight, margin = 15.dp)
            }
        )
    }
}