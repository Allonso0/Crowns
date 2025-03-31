package com.example.crowns.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data-класс, представляющий статистику игры Crowns.
 */
@Entity(tableName = "crowns_stats")
data class CrownsStats(
    @PrimaryKey val id: Int = 1,
    val startedGames: Int = 0,
    val wins: Int = 0,
    val bestScore: Int = 0,
    val bestTime: Long = 0L
)