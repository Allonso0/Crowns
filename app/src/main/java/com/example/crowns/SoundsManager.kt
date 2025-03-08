package com.example.crowns

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

class SoundsManager(context: Context) {
    private val soundPool: SoundPool
    private val sounds = mutableMapOf<Int, Int>()

    init {
        // Настраиваем аудиоатрибуты для SoundPool.
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME) // Для игровых звуков.
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        // Создаем SoundPool с ограничением в 4 потока.
        // TODO: возможно потребуется расширить с добавлением новых звуков.
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attributes)
            .build()

        // Используемые звуки
        sounds[R.raw.place] = soundPool.load(context, R.raw.place, 1)
        sounds[R.raw.hint] = soundPool.load(context, R.raw.hint, 1)
        sounds[R.raw.replay] = soundPool.load(context, R.raw.replay, 1)
        sounds[R.raw.erase] = soundPool.load(context, R.raw.erase, 1)
    }

    /**
     * Функция playSound отвечает за воспроизведение звука по ID ресурса.
     */
    fun playSound(soundResId: Int) {
        val soundId = sounds[soundResId] ?: return
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    /**
     * Функция release отвечает за освобождение ресурсов SoundPool.
     */
    fun release() {
        soundPool.release()
    }
}