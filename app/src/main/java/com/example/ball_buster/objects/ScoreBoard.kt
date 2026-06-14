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

    //속채우기
    private val fillPaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
        isAntiAlias = true
        isFakeBoldText = true
        style = Paint.Style.FILL
    }

    //테두리
    private val strokePaint = Paint().apply {
        color = Color.BLACK
        textSize = 60f
        isAntiAlias = true
        isFakeBoldText = true
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawText("STAGE: $stage", 50f, 80f, strokePaint)
        canvas.drawText("STAGE: $stage", 50f, 80f, fillPaint)

        canvas.drawText("SCORE: $score", 50f, 150f, strokePaint)
        canvas.drawText("SCORE: $score", 50f, 150f, fillPaint)

        canvas.drawText("LIVES: $lives", 50f, 220f, strokePaint)
        canvas.drawText("LIVES: $lives", 50f, 220f, fillPaint)
    }
}