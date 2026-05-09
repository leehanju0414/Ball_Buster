package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainLayer

class CollisionChecker(
    val world: World<MainLayer>
) : IGameObject {
    override fun update(gctx: GameContext) {
        checkHarpoonBallCollision(gctx)
    }

    private fun checkHarpoonBallCollision(gctx: GameContext) {
        val harpoons = world.objectsAt(MainLayer.HARPOON)
        val balls = world.objectsAt(MainLayer.BALL)

        for (i in harpoons.lastIndex downTo 0) {
            val harpoon = harpoons[i] as? Harpoon ?: continue

            for (j in balls.lastIndex downTo 0) {
                val ball = balls[j] as? Ball ?: continue

                if (RectF.intersects(harpoon.boundingBox, ball.boundingBox)) {
                    ball.split(gctx)
                    world.remove(harpoon, MainLayer.HARPOON)

                    break
                }
            }
        }
    }

    override fun draw(canvas: Canvas) {
    }
}