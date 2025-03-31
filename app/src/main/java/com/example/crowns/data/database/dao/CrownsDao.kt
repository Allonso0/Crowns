package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crowns.data.database.entity.CrownsState

/**
 * DAO для работы с состоянием игрового поля.
 */
@Dao
interface CrownsDao {
    // Получение последнего сохраненного состояния игры по ID.
    @Query("SELECT * FROM crowns_state WHERE id = 1")
    suspend fun getState(): CrownsState?

    // Сохранение состояния игрового поля.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveState(state: CrownsState)
}