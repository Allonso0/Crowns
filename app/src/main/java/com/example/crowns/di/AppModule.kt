package com.example.crowns.di

import android.content.Context
import androidx.room.Room
import com.example.crowns.data.database.AppDatabase
import com.example.crowns.data.database.dao.KillerSudokuDao
import com.example.crowns.data.database.dao.KillerSudokuStatsDao
import com.example.crowns.data.database.entity.KillerSudokuStats
import com.example.crowns.data.repository.KillerSudokuRepository
import com.example.crowns.domain.logic.SudokuGenerator
import com.example.crowns.domain.logic.SudokuValidator
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

    @Provides
    @Singleton
    fun provideKillerSudokuDao(database: AppDatabase) = database.killerSudokuDao()

    @Provides
    @Singleton
    fun provideKillerSudokuStatsDao(database: AppDatabase): KillerSudokuStatsDao = database.killerSudokuStatsDao()

    // Утилиты
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    // Бизнес-логика
    @Provides
    @Singleton
    fun provideSudokuGenerator(): SudokuGenerator = SudokuGenerator()

    @Provides
    @Singleton
    fun provideSudokuValidator(): SudokuValidator = SudokuValidator()

    // Репозиторий
    @Provides
    @Singleton
    fun provideKillerSudokuRepo(
        dao: KillerSudokuDao
    ): IKillerSudokuRepository = KillerSudokuRepository(dao)

    @Provides
    @Singleton
    fun provideInitialStats(database: AppDatabase) = CoroutineScope(Dispatchers.IO).launch {
        if (database.killerSudokuStatsDao().getStats() == null) {
            database.killerSudokuStatsDao().insertStats(KillerSudokuStats())
        }
    }
}