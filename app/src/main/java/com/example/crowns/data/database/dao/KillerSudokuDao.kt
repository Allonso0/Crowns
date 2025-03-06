package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.crowns.data.database.entity.KillerSudokuState

@Dao
interface KillerSudokuDao {
    @Query("SELECT * FROM killer_sudoku_states WHERE id = 1")
    suspend fun getState(): KillerSudokuState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveState(state: KillerSudokuState)
}