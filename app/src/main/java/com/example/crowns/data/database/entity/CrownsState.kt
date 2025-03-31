package com.example.crowns.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crowns.domain.model.CellState

/**
 * Data-класс, представляющий состояние игрового поля Crowns.
 */
@Entity(tableName = "crowns_state")
data class CrownsState(
    @PrimaryKey val id: Int = 1,
    val size: Int,
    val cells: List<List<CellState>>,
    val regions: Map<Int, List<Pair<Int, Int>>>,
    val isGameCompleted: Boolean = false,
    val elapsedTime: Long = 0
)