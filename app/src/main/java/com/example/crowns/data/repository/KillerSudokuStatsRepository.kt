package com.example.crowns.data.repository

import android.util.Log
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuStats
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы со статистикой Killer Sudoku.
 */
@Singleton
class KillerSudokuStatsRepository @Inject constructor(
    private val statisticDao: KillerSudokuStatsDao // Внедрение DAO.
) {
    // Получение статистики. Еесли нет в БД, то возвращаем дефолтный объект.
    suspend fun getStats(): KillerSudokuStats {
        return statisticDao.getStats() ?: KillerSudokuStats()
    }

    // Обновление статистики.
    suspend fun updateStats(block: KillerSudokuStats.() -> KillerSudokuStats) {
        val current = statisticDao.getStats() ?: KillerSudokuStats()

        // Применение изменений и сохранение.
        statisticDao.insertStats(current.block())
    }
}