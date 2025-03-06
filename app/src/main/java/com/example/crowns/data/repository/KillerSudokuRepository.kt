package com.example.crowns.data.repository

import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.entity.KillerSudokuState
import com.example.crowns.domain.repository.IKillerSudokuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KillerSudokuRepository @Inject constructor(
    private val dao: KillerSudokuDao
) : IKillerSudokuRepository {

    override suspend fun saveState(state: KillerSudokuState) {
        dao.saveState(state)
    }

    override suspend fun loadState(): KillerSudokuState? {
        return dao.getState()
    }
}