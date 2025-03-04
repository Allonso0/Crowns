package com.example.crowns.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CrownsVM : ViewModel() {

    var score by mutableFloatStateOf(0f)
    var timerMinutes by mutableFloatStateOf(0f)
    var timerSeconds by mutableFloatStateOf(0f)

}