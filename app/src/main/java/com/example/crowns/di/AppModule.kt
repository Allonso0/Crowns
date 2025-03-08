package com.example.crowns.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.crowns.data.database.AppDatabase
import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.dao.KillerSudokuSettingsDao
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuSettings
import com.example.crowns.data.database.entity.KillerSudokuStats
import com.example.crowns.data.repository.KillerSudokuRepository
import com.example.crowns.domain.logic.SudokuGenerator
import com.example.crowns.domain.logic.SudokuValidator
import com.example.crowns.domain.model.Difficulty
import com.example.crowns.domain.repository.IKillerSudokuRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * Модуль Hilt для предоставления зависимостей.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Database и DAO
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "crowns-db"
        ).fallbackToDestructiveMigration().build()
    }

    // DAO для работы с состоянием игрового поля.
    @Provides
    @Singleton
    fun provideKillerSudokuDao(database: AppDatabase) = database.killerSudokuDao()

    // DAO для работы со статистикой.
    @Provides
    @Singleton
    fun provideKillerSudokuStatsDao(database: AppDatabase): KillerSudokuStatsDao = database.killerSudokuStatsDao()

    // DAO для работы с настройками.
    @Provides
    @Singleton
    fun provideKillerSudokuSettingsDao(database: AppDatabase): KillerSudokuSettingsDao = database.killerSudokuSettingsDao()

    // Утилита для JSON-сериализации.
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    // Генератор судоку (бизнес-логика).
    @Provides
    @Singleton
    fun provideSudokuGenerator(): SudokuGenerator = SudokuGenerator()

    // Валидатор судоку (бизнес-логика).
    @Provides
    @Singleton
    fun provideSudokuValidator(): SudokuValidator = SudokuValidator()

    // Репозиторий для работы с состоянием игрового поля.
    @Provides
    @Singleton
    fun provideKillerSudokuRepo(
        dao: KillerSudokuDao
    ): IKillerSudokuRepository = KillerSudokuRepository(dao)

    // Вставка дефолтных параметров статистики если их нет в БД.
    @Provides
    @Singleton
    fun provideInitialStats(database: AppDatabase) = CoroutineScope(Dispatchers.IO).launch {
        if (database.killerSudokuStatsDao().getStats() == null) {
            database.killerSudokuStatsDao().insertStats(KillerSudokuStats())
        }
    }

    // Вставка дефолтных параметров настроек если их нет в БД.
    @Provides
    @Singleton
    fun provideInitialSettings(database: AppDatabase) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val count = database.killerSudokuSettingsDao().settingsCount()
            if (count == 0) {
                database.killerSudokuSettingsDao().insertKillerSudokuSettings(
                    KillerSudokuSettings(
                        difficulty = Difficulty.MEDIUM,
                        showTimer = true,
                        errorLimitEnabled = true,
                        soundEnabled = true,
                        highlightSameNumbers = true,
                        highlightErrors = true
                    )
                )
                Log.d("Settings", "Default settings inserted")
            }
        } catch (e: Exception) {
            Log.e("Settings", "Error initializing settings", e)
        }
    }
}