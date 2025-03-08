package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crowns.data.database.entity.KillerSudokuState

/**
 * DAO для работы с состоянием игрового поля.
 */
@Dao
interface KillerSudokuDao {
    // Получение последнего сохраненного состояния игры по ID.
    @Query("SELECT * FROM killer_sudoku_states WHERE id = 1")
    suspend fun getState(): KillerSudokuState?

    // Сохранение состояния игрового поля.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveState(state: KillerSudokuState)
}