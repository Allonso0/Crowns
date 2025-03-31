package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crowns.data.database.entity.CrownsSettings
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с настройками игры.
 */
@Dao
interface CrownsSettingsDao {
    // Получение настроек в виде потока.
    @Query("SELECT * FROM crowns_settings WHERE id = 1")
    fun getSettings(): Flow<CrownsSettings>

    // Вставка или замена настроек (при конфликте ID).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: CrownsSettings)

    // Проверка наличия записей в таблице (для инициализации).
    @Query("SELECT COUNT(*) FROM crowns_settings WHERE id = 1")
    suspend fun settingsCount(): Int
}