package com.example.crowns.data.repository

import com.example.crowns.data.database.dao.CrownsSettingsDao
import com.example.crowns.data.database.entity.CrownsSettings
import com.example.crowns.data.database.entity.KillerSudokuSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы с настройками Crowns.
 */
@Singleton
class CrownsSettingsRepository @Inject constructor(
    private val settingsDao: CrownsSettingsDao
){
    // Получаем настройки в виде потока данных Flow.
    fun getSettings(): Flow<CrownsSettings> = settingsDao.getSettings()

    // Обновление настроек.
    suspend fun updateSettings(block: (CrownsSettings) -> CrownsSettings) {
        val current = settingsDao.getSettings().first()
        val newSettings = block(current ?: CrownsSettings())
        settingsDao.insertSettings(newSettings)
    }
}