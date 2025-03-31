package com.example.crowns.data.repository

import com.example.crowns.data.database.dao.CrownsStatsDao
import com.example.crowns.data.database.entity.CrownsStats
import javax.inject.Inject

/**
 * Репозиторий для работы со статистикой Crowns.
 */
class CrownsStatsRepository @Inject constructor(
    private val statsDao: CrownsStatsDao // Внедрение DAO.
) {
    // Получение статистики. Еесли нет в БД, то возвращаем дефолтный объект.
    suspend fun getStats(): CrownsStats {
        return statsDao.getStats() ?: CrownsStats()
    }

    // Обновление статистики.
    suspend fun updateStats(block: CrownsStats.() -> CrownsStats) {
        val current = statsDao.getStats() ?: CrownsStats()
        statsDao.insertStats(current.block())
    }
}