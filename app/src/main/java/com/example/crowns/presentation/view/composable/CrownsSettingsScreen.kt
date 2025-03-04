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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.crowns.R
import com.example.crowns.presentation.viewmodel.CrownsSettingsVM
import kotlin.math.roundToInt

@Composable
fun CrownsSettingsScreen(navController: NavController, vm: CrownsSettingsVM = viewModel()) {
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
            textVolume,
            textTimer,
            textExplain,
            innerLine,
            switch1,
            switch2,
            switch3,
            slider
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
                text = "Настройки",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
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
                text = "Размер игрового поля: ${vm.getSliderPos().roundToInt()}",
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
                value = vm.getSliderPos(),
                valueRange = 5f..12f,
                steps = 6,
                onValueChange = { vm.changeSliderPos(it) },
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
                .height(90.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                .background(color = Color.White)
                .constrainAs(box2) {
                    centerHorizontallyTo(parent)
                    top.linkTo(box1.bottom, margin = 20.dp)
                },
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
                text = "Авторасстановка \"X\"",
                color = colorResource(R.color.backgroundDark),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Box(
            modifier = Modifier
                .width(290.dp)
                .constrainAs(textExplain) {
                    absoluteLeft.linkTo(box3.absoluteLeft, margin = 15.dp)
                    top.linkTo(box3.bottom, margin = 5.dp)
                }
        ) {
            Text(
                text = "Автоматически расставлять \"X\" в " +
                        "клетках, куда нельзя установить новые фигуры",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.alpha(0.48f)
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
            checked = vm.checkedState1,
            onCheckedChange = {
                vm.changeState1(it)
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
            checked = vm.checkedState2,
            onCheckedChange = {
                vm.changeState2(it)
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

        Switch(
            checked = vm.checkedState3,
            onCheckedChange = {
                vm.changeState3(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.backgroundDark),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.LightGray,
                uncheckedBorderColor = Color.LightGray
            ),
            modifier = Modifier
                .constrainAs(switch3) {
                    centerVerticallyTo(box3)
                    absoluteRight.linkTo(box3.absoluteRight, margin = 15.dp)
                }
        )
    }
}