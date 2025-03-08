package com.example.crowns.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.dao.KillerSudokuSettingsDao
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.data.database.entity.KillerSudokuState
import com.example.crowns.data.database.entity.KillerSudokuStats

/**
 * Главный класс базы данных Room.
 */
@Database(
    // Сущности (таблицы).
    entities = [
        KillerSudokuState::class,
        KillerSudokuStats::class,
        KillerSudokuSettings::class
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // DAO для работы с состоянием игрового поля.
    abstract fun killerSudokuDao(): KillerSudokuDao

    // DAO для статистики.
    abstract fun killerSudokuStatsDao(): KillerSudokuStatsDao

    // DAO для настроек.
    abstract fun killerSudokuSettingsDao(): KillerSudokuSettingsDao
}