package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.crowns.data.database.entity.KillerSudokuStats

/**
 * DAO для работы со статистикой игрока.
 */
@Dao
interface KillerSudokuStatsDao {
    // Получение статистики по ID.
    @Query("SELECT * FROM killer_sudoku_stats WHERE id = 1")
    suspend fun getStats(): KillerSudokuStats?

    // Вставка или замена статистики (при конфликте ID).
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: KillerSudokuStats)

    // Обновление существующей статистики.
    @Update
    suspend fun updateStats(stats: KillerSudokuStats)
}