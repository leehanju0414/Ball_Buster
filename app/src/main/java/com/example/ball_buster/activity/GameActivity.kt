package com.example.ball_buster.activity

import android.os.Bundle
import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainScene
class GameActivity : BaseGameActivity() {
    companion object {
        var instance: GameActivity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    override fun createRootScene(gctx: GameContext): Scene {
        gctx.metrics.setSize(1600f, 900f)
        return MainScene(gctx)
    }
}