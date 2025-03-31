package com.example.crowns.data.repository

import com.example.crowns.data.database.dao.CrownsDao
import com.example.crowns.data.database.entity.CrownsState
import com.example.crowns.domain.repository.ICrownsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Репозиторий для работы с состоянием игрового поля Crowns.
 */
@Singleton
class CrownsRepository @Inject constructor(
    private val dao: CrownsDao
) : ICrownsRepository {

    // Сохранение текущего состояния игрового поля.
    override suspend fun saveState(state: CrownsState) {
        dao.saveState(state)
    }

    // Загрузка последнего сохраненного состояния.
    override suspend fun loadState(): CrownsState? {
        return dao.getState()
    }
}