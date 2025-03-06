package com.example.crowns.data.database

import androidx.room.TypeConverter
import com.example.crowns.domain.model.KillerSudokuBoard
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    fun longToElapsedTime(value: Long?): Long = value ?: 0L

    @TypeConverter
    fun elapsedTimeToLong(value: Long): Long = value
}