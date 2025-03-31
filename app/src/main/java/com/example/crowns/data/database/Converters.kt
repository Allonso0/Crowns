package com.example.crowns.data.database

import androidx.room.TypeConverter
import com.example.crowns.domain.model.CellState
import com.example.crowns.domain.model.KillerSudokuBoard
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Конвертеры для библиотеки Room для преобразования
// сложных объектов в строки (и обратно).
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun boardToString(board: KillerSudokuBoard): String {
        return gson.toJson(board)
    }

    @TypeConverter
    fun stringToBoard(json: String): KillerSudokuBoard {
        return gson.fromJson(json, KillerSudokuBoard::class.java)
    }

    @TypeConverter
    fun solutionToString(solution: List<List<Int>>): String {
        return gson.toJson(solution)
    }

    @TypeConverter
    fun stringToSolution(json: String): List<List<Int>> {
        return gson.fromJson(json, object : TypeToken<List<List<Int>>>() {}.type)
    }

    @TypeConverter
    fun hintCellsToString(hints: List<Pair<Int, Int>>): String {
        return gson.toJson(hints)
    }

    @TypeConverter
    fun stringToHintCells(json: String): List<Pair<Int, Int>> {
        return gson.fromJson(json, object : TypeToken<List<Pair<Int, Int>>>() {}.type)
    }

    @TypeConverter
    fun cellStateListToString(list: List<List<CellState>>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToCellStateList(json: String): List<List<CellState>> {
        return gson.fromJson(json, object: TypeToken<List<List<CellState>>>() {}.type)
    }

    @TypeConverter
    fun regionsMapToString(regions: Map<Int, List<Pair<Int, Int>>>): String {
        val serialized = regions.mapValues { (_, cells) ->
            cells.map { listOf(it.first, it.second) }
        }
        return gson.toJson(serialized)
    }

    @TypeConverter
    fun stringToRegionMap(json: String): Map<Int, List<Pair<Int, Int>>> {
        val type = object : TypeToken<Map<Int, List<List<Int>>>>() {}.type
        val intermediate = gson.fromJson<Map<Int, List<List<Int>>>>(json, type)

        return intermediate.mapValues { (_, cells) ->
            cells.map { list ->
                require(list.size == 2) { "Invalid cell coordinates" }
                list[0] to list[1]
            }
        }
    }
}