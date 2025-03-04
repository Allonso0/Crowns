package com.example.crowns.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CrownsSettingsVM : ViewModel() {
    var sliderPosition by mutableFloatStateOf(5f)

    var checkedState1 by mutableStateOf(false)
    var checkedState2 by mutableStateOf(false)
    var checkedState3 by mutableStateOf(false)

    fun getSliderPos() : Float {
        return sliderPosition
    }

    fun changeSliderPos(value: Float) {
        sliderPosition = value
    }

    fun changeState1(value: Boolean) {
        checkedState1 = value
    }

    fun changeState2(value: Boolean) {
        checkedState2 = value
    }

    fun changeState3(value: Boolean) {
        checkedState3 = value
    }
}