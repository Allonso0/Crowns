package com.example.crowns.data.repository

import com.example.crowns.data.database.dao.KillerSudokuSettingsDao
import com.example.crowns.data.database.entity.KillerSudokuSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы с настройками Killer Sudoku.
 */
@Singleton
class KillerSudokuSettingsRepository @Inject constructor(
    private val killerSudokuSettingsDao: KillerSudokuSettingsDao
) {
    // Получаем настройки в виде потока данных Flow.
    fun getSettings(): Flow<KillerSudokuSettings> = killerSudokuSettingsDao.getKillerSudokuSettings()

    // Обновление настроек.
    suspend fun updateSettings(block: (KillerSudokuSettings) -> KillerSudokuSettings) {
        val current = killerSudokuSettingsDao.getKillerSudokuSettings().first()
        val newSettings = block(current ?: KillerSudokuSettings())
        killerSudokuSettingsDao.insertKillerSudokuSettings(newSettings)
    }
}