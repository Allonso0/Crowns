package com.example.crowns

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // Аннотация для того, чтобы интегрировать Hilt в приложение.
class CrownsApplication : Application() {
    // Менеджер звуков. Инициализируется при создании.
    lateinit var soundManager: SoundsManager

    override fun onCreate() {
        super.onCreate()
        soundManager = SoundsManager(this)
    }
}