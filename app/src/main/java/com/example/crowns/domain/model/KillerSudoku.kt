package com.example.crowns.domain.model

data class KillerSudokuCell(
    val value: Int? = null,
    val isFixed: Boolean,
    val cageId: Int,
    val cageSum: Int,
    val isError: Boolean = false,
    val isHint: Boolean = false
)

data class KillerSudokuBoard(
    val cells: List<List<KillerSudokuCell>>,
    val cages: Map<Int, List<Pair<Int, Int>>>,
)