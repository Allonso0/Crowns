package com.example.crowns.domain.repository

import com.example.crowns.data.database.entity.CrownsState

/**
 * Интерфейс репозитория для работы с состоянием игры Crowns.
 */
interface ICrownsRepository {
    // Сохранение состояния.
    suspend fun saveState(state: CrownsState)

    // Загрузка состояния.
    suspend fun loadState(): CrownsState?
}