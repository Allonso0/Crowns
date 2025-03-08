package com.example.crowns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.data.database.entity.KillerSudokuStats
import com.example.crowns.data.repository.KillerSudokuStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsVM @Inject constructor(
    private val statisticRepository: KillerSudokuStatsRepository
) : ViewModel() {
    // Лениво ицициализируем поток данных для статистики.
    val stats = flow {
        emit(statisticRepository.getStats()) // Отправка данных в поток.
    }.stateIn(viewModelScope,
        SharingStarted.Lazily, // Сохраняем состояние даже без активных подписчиков.
        KillerSudokuStats() // Дефолтное значение.
    )
}