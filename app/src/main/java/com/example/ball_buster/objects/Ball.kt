package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Ball(val gctx: GameContext, var x: Float, var y: Float, var dx: Float) : IGameObject {

    val radius = 80f

    var dy = 0f
    val gravity = 1500f
    val bounceY = -900f
    val groundY = 850f

    private val paint = Paint().apply { color = Color.RED }
    private val rect = RectF()

    override fun update(gctx: GameContext) {
        val dt = gctx.frameTime

        dy += gravity * dt

        x += dx * dt
        y += dy * dt

        if (y + radius > groundY) {
            y = groundY - radius
            dy = bounceY
        }

        if (x - radius < 0) {
            x = radius
            dx = -dx
        } else if (x + radius > gctx.metrics.width) {
            x = gctx.metrics.width - radius
            dx = -dx
        }

        rect.set(x - radius, y - radius, x + radius, y + radius)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawOval(rect, paint)
    }
}