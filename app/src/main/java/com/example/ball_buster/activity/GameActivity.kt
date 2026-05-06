package com.example.ball_buster.activity

import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainScene
class GameActivity : BaseGameActivity() {
    override val drawsDebugGrid: Boolean = true
    override val drawsDebugInfo: Boolean = true
    override val drawsFpsGraph: Boolean = true

    override fun createRootScene(gctx: GameContext): Scene {
        gctx.metrics.setSize(1600f, 900f)
        return MainScene(gctx)
    }
}