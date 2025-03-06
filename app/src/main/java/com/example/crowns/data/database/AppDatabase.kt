package com.example.crowns.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuState
import com.example.crowns.data.database.entity.KillerSudokuStats

@Database(
    entities = [
        KillerSudokuState::class,
        KillerSudokuStats::class
               ],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun killerSudokuDao(): KillerSudokuDao

    abstract fun killerSudokuStatsDao(): KillerSudokuStatsDao
}