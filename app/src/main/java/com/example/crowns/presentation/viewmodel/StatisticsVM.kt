package com.example.crowns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crowns.data.database.entity.CrownsStats
import com.example.crowns.data.database.entity.KillerSudokuStats
import com.example.crowns.data.repository.CrownsStatsRepository
import com.example.crowns.data.repository.KillerSudokuStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsVM @Inject constructor(
    private val killerSudokuStatisticRepo: KillerSudokuStatsRepository,
    private val crownsStatisticsRepo: CrownsStatsRepository
) : ViewModel() {
    // Лениво ицициализируем поток данных для статистики.
    val statsKS = flow {
        emit(killerSudokuStatisticRepo.getStats()) // Отправка данных в поток.
    }.stateIn(viewModelScope,
        SharingStarted.Lazily, // Сохраняем состояние даже без активных подписчиков.
        KillerSudokuStats() // Дефолтное значение.
    )

    val statsCrowns = flow {
        emit(crownsStatisticsRepo.getStats())
    }.stateIn(viewModelScope,
        SharingStarted.Lazily,
        CrownsStats()
    )
}