package com.example.crowns.domain.model

data class CrownsCell(
    var state: CellState = CellState.EMPTY,
    val regionID: Int,
    val isError: Boolean = false
)

data class CrownsBoard(
    val size: Int,
    val cells: List<List<CrownsCell>>,
    val regions: Map<Int, List<Pair<Int, Int>>>
)