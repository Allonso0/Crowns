package com.example.crowns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.data.database.entity.CrownsSettings
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.data.repository.CrownsSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrownsSettingsVM @Inject constructor(
    private val repository: CrownsSettingsRepository
) : ViewModel() {
    // Создаем поток настроек с дефолтным значением.
    val settings: StateFlow<CrownsSettings> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Состояние сохраняется 5 сек после отписки.
            initialValue = CrownsSettings() // Передаем дефолтные настройки.
        )

    fun setBoardSize(size: Int) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(boardSize = size) }
        }
    }

    fun setTimerEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(showTimer = enabled) }
        }
    }

    fun setAutoCrossEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(autoCrossEnabled = enabled) }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { it.copy(soundEnabled = enabled) }
        }
    }
}