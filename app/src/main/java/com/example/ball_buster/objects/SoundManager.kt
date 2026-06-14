package com.example.ball_buster.objects

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.ball_buster.R

object SoundManager {
    private lateinit var soundPool: SoundPool
    private var bgmPlayer: MediaPlayer? = null

    //효과음 ID들을 저장할 변수
    var sfxFire = 0
    var sfxHit = 0
    var sfxExplosion = 0
    var sfxClear = 0

    fun init(context: Context) {
        soundPool = SoundPool.Builder().setMaxStreams(10).build()

        sfxFire = soundPool.load(context, R.raw.sfx_fire, 1)
        sfxHit = soundPool.load(context, R.raw.sfx_hit, 1)
        sfxExplosion = soundPool.load(context, R.raw.sfx_explosion, 1)
        sfxClear = soundPool.load(context, R.raw.sfx_clear, 1)
    }

    fun playSfx(soundId: Int) {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    fun playBgm(context: Context, resId: Int) {
        bgmPlayer?.stop()
        bgmPlayer?.release() // 기존 음악 끄기

        bgmPlayer = MediaPlayer.create(context, resId)
        bgmPlayer?.isLooping = true // BGM은 무한 반복
        bgmPlayer?.start()
    }

    fun stopBgm() {
        bgmPlayer?.stop()
    }

    fun pauseBgm() {
        if (bgmPlayer?.isPlaying == true) {
            bgmPlayer?.pause()
        }
    }

    fun resumeBgm() {
        if (bgmPlayer != null && bgmPlayer?.isPlaying == false) {
            bgmPlayer?.start()
        }
    }
}