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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.crowns.R

@Composable
fun KillerSudokuSettingsScreen(navController: NavController) {
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
            box5,
            box6,
            switch1,
            switch2,
            switch3,
            switch4,
            switch5,
            switch6,
        ) = createRefs()

        val (
            explain1,
            explain2,
            explain3,
            explain4,
            textVolume,
            textTimer,
            innerLine
        ) = createRefs()

        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }
        val checkedState4 = remember { mutableStateOf(false) }
        val checkedState5 = remember { mutableStateOf(false) }
        val checkedState6 = remember { mutableStateOf(false) }

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
                text = "Сложность игры",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(90.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box2) {
                    centerHorizontallyTo(parent)
                    top.linkTo(box1.bottom, margin = 20.dp)
                },
        )

        Box (
            modifier = Modifier
                .width(290.dp)
                .height(1.dp)
                .alpha(0.35f)
                .background(Color.Black)
                .constrainAs(innerLine) {
                    centerTo(box2)
                }
        )

        Box (
            modifier = Modifier.constrainAs(textVolume) {
                top.linkTo(box2.top, margin = 15.dp)
                absoluteLeft.linkTo(box2.absoluteLeft, margin = 15.dp)
            }
        ) {
            Text(
                text = "Звук",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }

        Box (
            modifier = Modifier.constrainAs(textTimer) {
                bottom.linkTo(box2.bottom, margin = 15.dp)
                absoluteLeft.linkTo(box2.absoluteLeft, margin = 15.dp)
            }
        ) {
            Text(
                text = "Таймер",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
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
                top.linkTo(box2.top)
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
                bottom.linkTo(box2.bottom)
                absoluteRight.linkTo(box2.absoluteRight, margin = 15.dp)
            }
        )

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box3) {
                    centerHorizontallyTo(parent)
                    top.linkTo(box2.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Лимит ошибок",
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
                    absoluteLeft.linkTo(box3.absoluteLeft, margin = 15.dp)
                    top.linkTo(box3.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Вкл/откл лимит ошибок в игре",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

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
                centerVerticallyTo(box3)
                absoluteRight.linkTo(box3.absoluteRight, margin = 15.dp)
            }
        )

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box4) {
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
                    absoluteLeft.linkTo(box4.absoluteLeft, margin = 15.dp)
                    top.linkTo(box4.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Помечать цифры, которые не соответствуют финальному решению судоку",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Switch(
            checked = checkedState4.value,
            onCheckedChange = {
                checkedState4.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch4) {
                centerVerticallyTo(box4)
                absoluteRight.linkTo(box4.absoluteRight, margin = 15.dp)
            }
        )

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box5) {
                    centerHorizontallyTo(parent)
                    top.linkTo(explain2.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Выделять одинак. цифры",
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
                    absoluteLeft.linkTo(box5.absoluteLeft, margin = 15.dp)
                    top.linkTo(box5.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "При выборе ячейки с цифрой выделять эти же цифры на всём игровом поле",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Switch(
            checked = checkedState5.value,
            onCheckedChange = {
                checkedState5.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch5) {
                centerVerticallyTo(box5)
                absoluteRight.linkTo(box5.absoluteRight, margin = 15.dp)
            }
        )

        Box(
            modifier = Modifier
                .width(320.dp)
                .height(45.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box6) {
                    centerHorizontallyTo(parent)
                    top.linkTo(explain3.bottom, margin = 20.dp)
                },
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Выделять повторы",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(explain4) {
                    absoluteLeft.linkTo(box6.absoluteLeft, margin = 15.dp)
                    top.linkTo(box6.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Выделять повторяющиеся цифры в строках, столбцах и блоках",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
            )
        }

        Switch(
            checked = checkedState6.value,
            onCheckedChange = {
                checkedState6.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier.constrainAs(switch6) {
                centerVerticallyTo(box6)
                absoluteRight.linkTo(box6.absoluteRight, margin = 15.dp)
            }
        )
    }
}