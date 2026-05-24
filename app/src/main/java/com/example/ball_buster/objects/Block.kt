package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Block(val x: Float, val y: Float, val width: Float, val height: Float) : IGameObject {

    val boundingBox = RectF(x, y, x + width, y + height)

    private val paint = Paint().apply { color = Color.DKGRAY }

    override fun update(gctx: GameContext) {
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(boundingBox, paint)
    }
}