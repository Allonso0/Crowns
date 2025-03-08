package com.example.crowns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.data.repository.KillerSudokuSettingsRepository
import com.example.crowns.domain.model.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KillerSudokuSettingsVM @Inject constructor(
    private val repository: KillerSudokuSettingsRepository
) : ViewModel() {
    // Создаем поток настроек с дефолтным значением.
    val settings: StateFlow<KillerSudokuSettings> = repository.getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Состояние сохраняется 5 сек после отписки.
            initialValue = KillerSudokuSettings() // Передаем дефолтные настройки.
        )

    // Обновление сложности игры.
    fun setDifficulty(difficulty: Difficulty) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(difficulty = difficulty) ?: KillerSudokuSettings(difficulty = difficulty)
            }
        }
    }

    // Включение/отключение лимита ошибок.
    fun setErrorLimitEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(errorLimitEnabled = enabled) ?: KillerSudokuSettings(errorLimitEnabled = enabled)
            }
        }
    }

    // Включение/отключение отображение таймера.
    fun setTimerEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(showTimer = enabled) ?: KillerSudokuSettings(showTimer = enabled)
            }
        }
    }

    // Включение/отключение звуков в игре.
    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(soundEnabled = enabled) ?: KillerSudokuSettings(soundEnabled = enabled)
            }
        }
    }

    // Включение/отключение подсветки одинаковых номеров.
    fun setHighlightSameNumbers(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(highlightSameNumbers = enabled) ?: KillerSudokuSettings(highlightSameNumbers = enabled)
            }
        }
    }

    // Включение/отключение отметки неправильно стоящих цифр.
    fun setHighlightErrors(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings { current ->
                current.copy(highlightErrors = enabled) ?: KillerSudokuSettings(highlightSameNumbers = enabled)
            }
        }
    }
}