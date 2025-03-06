package com.example.crowns.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.crowns.data.database.entity.KillerSudokuStats

@Dao
interface KillerSudokuStatsDao {
    @Query("SELECT * FROM killer_sudoku_stats WHERE id = 1")
    suspend fun getStats(): KillerSudokuStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: KillerSudokuStats)

    @Update
    suspend fun updateStats(stats: KillerSudokuStats)
}