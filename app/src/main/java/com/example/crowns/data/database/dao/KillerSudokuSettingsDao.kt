package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crowns.data.database.entity.KillerSudokuSettings
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с настройками игры.
 */
@Dao
interface KillerSudokuSettingsDao {
    // Получение настроек в виде потока.
    @Query("SELECT * FROM killer_sudoku_settings WHERE id = 1")
    fun getKillerSudokuSettings(): Flow<KillerSudokuSettings>

    // Вставка или замена настроек (при конфликте ID).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKillerSudokuSettings(settings: KillerSudokuSettings)

    // Проверка наличия записей в таблице (для инициализации).
    @Query("SELECT COUNT(*) FROM killer_sudoku_settings WHERE id = 1")
    suspend fun settingsCount(): Int
}