package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.ball_buster.scene.MainLayer
import com.example.ball_buster.scene.MainScene
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos

class Player(val gctx: GameContext, val joystick: JoyStick) : IGameObject {
    var x = 800f
    var y = 800f
    val radius = 50f
    val maxSpeed = 800f

    private val paint = Paint().apply { color = Color.BLUE }
    private val rect = RectF()

    fun fire() {
        val harpoon = Harpoon(x, y - radius)

        (gctx.scene as? MainScene)?.world?.add(harpoon, MainLayer.HARPOON)
    }

    override fun update(gctx: GameContext) {
        val dx = maxSpeed * joystick.power * cos(joystick.angle)

        x += dx * gctx.frameTime

        if (x < radius) x = radius
        if (x > 1600f - radius) x = 1600f - radius

        rect.set(x - radius, y - radius, x + radius, y + radius)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }
}