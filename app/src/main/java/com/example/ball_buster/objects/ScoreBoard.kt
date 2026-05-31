package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class ScoreBoard : IGameObject {
    var score = 0
    var lives = 3
    var stage = 1

    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        isAntiAlias = true
        isFakeBoldText = true
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText("STAGE: $stage", 50f, 80f, paint)
        canvas.drawText("SCORE: $score", 50f, 150f, paint)

        canvas.drawText("LIVES: $lives", 1300f, 80f, paint)
    }
}