package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.ball_buster.scene.MainLayer
import com.example.ball_buster.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.abs

class Ball(var x: Float, var y: Float, var dx: Float, val level: Int = 3) : IGameObject {
    // 레벨*튀기는 높이
    val radius = level * 30f
    val bounceY = -700f - (level * 100f)

    var dy = 0f
    val gravity = 1500f
    val groundY = 850f

    private val paint = Paint().apply { color = Color.RED }
    val boundingBox = RectF()

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

        // 공의 영역 업데이트
        boundingBox.set(x - radius, y - radius, x + radius, y + radius)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawOval(boundingBox, paint)
    }

    // 공 쪼개기
    fun split(gctx: GameContext) {
        val world = (gctx.scene as? MainScene)?.world ?: return

        world.remove(this, MainLayer.BALL)

        // 레벨 2 이상 분열
        if (level > 1) {
            val splitSpeed = abs(dx) * 1.2f // 쪼개지면 가속
            val splitBounce = -500f // 쪼개질 때 살짝 위로

            // 왼쪽
            val ball1 = Ball(x, y, -splitSpeed, level - 1)
            ball1.dy = splitBounce

            // 오른쪽
            val ball2 = Ball(x, y, splitSpeed, level - 1)
            ball2.dy = splitBounce

            world.add(ball1, MainLayer.BALL)
            world.add(ball2, MainLayer.BALL)
        }
    }
}