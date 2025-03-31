package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.crowns.data.database.entity.CrownsStats

/**
 * DAO для работы со статистикой игрока.
 */
@Dao
interface CrownsStatsDao {
    // Получение статистики по ID.
    @Query("SELECT * FROM crowns_stats WHERE id = 1")
    suspend fun getStats(): CrownsStats?

    // Вставка или замена статистики (при конфликте ID).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: CrownsStats)

    // Обновление существующей статистики.
    @Update
    suspend fun updateStats(stats: CrownsStats)
}