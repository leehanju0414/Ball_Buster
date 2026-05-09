package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.ball_buster.scene.MainLayer
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainScene

class Harpoon(var x: Float, var y: Float) : IGameObject {

    val speed = 1200f
    val width = 10f
    val length = 150f

    private val paint = Paint().apply { color = Color.YELLOW }

    val boundingBox = RectF()

    override fun update(gctx: GameContext) {
        y -= speed * gctx.frameTime
        boundingBox.set(x - width / 2, y, x + width / 2, y + length)

        val scene = gctx.scene as? MainScene
        val world = scene?.world ?: return

        if (y + length < 0) {
            world.remove(this, MainLayer.HARPOON)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(boundingBox, paint)
    }
}