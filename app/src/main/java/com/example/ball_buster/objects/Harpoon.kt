package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.ball_buster.scene.MainLayer
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainScene

class Harpoon(val gctx: GameContext, var x: Float, var y: Float) : IGameObject {
    val speed = 1200f
    val width = 10f
    val length = 150f

    private val paint = Paint().apply { color = Color.YELLOW }
    private val rect = RectF()

    override fun update(gctx: GameContext) {
        y -= speed * gctx.frameTime

        // 맵 넘어가면 삭제
        if (y + length < 0) {
            (gctx.scene as? MainScene)?.world?.remove(this, MainLayer.HARPOON)
        }

        rect.set(x - width / 2, y, x + width / 2, y + length)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }
}