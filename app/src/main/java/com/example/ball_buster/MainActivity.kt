package com.example.ball_buster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ball_buster.activity.GameActivity
import com.example.ball_buster.databinding.ActivityMainBinding
import com.example.ball_buster.objects.SoundManager

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SoundManager.init(this)

        binding.btnStartGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        SoundManager.playBgm(this, R.raw.bgm_main)
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseBgm()
    }
}