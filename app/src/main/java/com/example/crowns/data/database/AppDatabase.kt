package com.example.crowns.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.crowns.data.database.dao.CrownsDao
import com.example.crowns.data.database.dao.CrownsSettingsDao
import com.example.crowns.data.database.dao.CrownsStatsDao
import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.dao.KillerSudokuSettingsDao
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.CrownsSettings
import com.example.crowns.data.database.entity.CrownsState
import com.example.crowns.data.database.entity.CrownsStats
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
        KillerSudokuSettings::class,
        CrownsState::class,
        CrownsStats::class,
        CrownsSettings::class
    ],
    version = 19,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    // DAO для работы с состоянием игрового поля Killer Sudoku.
    abstract fun killerSudokuDao(): KillerSudokuDao

    // DAO для статистики Killer Sudoku.
    abstract fun killerSudokuStatsDao(): KillerSudokuStatsDao

    // DAO для настроек Killer Sudoku.
    abstract fun killerSudokuSettingsDao(): KillerSudokuSettingsDao

    // DAO для работы с состоянием игрового поля Crowns.
    abstract fun crownsDao(): CrownsDao

    // DAO для статистики Crowns.
    abstract fun crownsStatsDao(): CrownsStatsDao

    // DAO для настроек Crowns.
    abstract fun crownsSettingsDao(): CrownsSettingsDao
}