package com.example.crowns.data.repository

import com.example.crowns.domain.repository.IKillerSudokuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KillerSudokuRepository @Inject constructor() : IKillerSudokuRepository {
    // Репозиторий для взаимодействия с БД. Пока пустой.
    // TODO: сохранение игрового поля и статистики в Room
}