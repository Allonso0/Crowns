package com.example.crowns.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data-класс, представляющий настройки игры Crowns.
 */
@Entity(tableName = "crowns_settings")
data class CrownsSettings(
    @PrimaryKey val id: Int = 1,
    val boardSize: Int = 5,
    val showTimer: Boolean = true,
    val autoCrossEnabled: Boolean = true,
    val soundEnabled: Boolean = true
)