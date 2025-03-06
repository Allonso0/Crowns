package com.example.crowns.data.repository

import android.util.Log
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuStats
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KillerSudokuStatsRepository @Inject constructor(
    private val statisticDao: KillerSudokuStatsDao
) {
    suspend fun getStats(): KillerSudokuStats {
        return statisticDao.getStats() ?: KillerSudokuStats()
    }

    suspend fun updateStats(block: KillerSudokuStats.() -> KillerSudokuStats) {
        val current = statisticDao.getStats() ?: KillerSudokuStats()
        Log.d("Stats", "Updating bestTime: ${current.block().bestTime} ms")
        statisticDao.insertStats(current.block())
    }
}