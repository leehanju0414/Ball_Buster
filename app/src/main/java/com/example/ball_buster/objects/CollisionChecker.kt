package com.example.ball_buster.objects

import android.graphics.Canvas
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.IGameObject
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.scene.MainLayer
import com.example.ball_buster.scene.MainScene

class CollisionChecker(
    val world: World<MainLayer>
) : IGameObject {
    override fun update(gctx: GameContext) {
        checkHarpoonBallCollision(gctx)
        checkHarpoonBlockCollision()
        checkBallBlockCollision()
        checkPlayerBallCollision(gctx)
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

    private fun checkHarpoonBlockCollision() {
        val harpoons = world.objectsAt(MainLayer.HARPOON)
        val blocks = world.objectsAt(MainLayer.BLOCK)

        for (i in harpoons.lastIndex downTo 0) {
            val harpoon = harpoons[i] as? Harpoon ?: continue

            for (j in blocks.lastIndex downTo 0) {
                val block = blocks[j] as? Block ?: continue

                if (RectF.intersects(harpoon.boundingBox, block.boundingBox)) {
                    world.remove(harpoon, MainLayer.HARPOON)
                    break
                }
            }
        }
    }

    private fun checkBallBlockCollision() {
        val balls = world.objectsAt(MainLayer.BALL)
        val blocks = world.objectsAt(MainLayer.BLOCK)

        for (i in balls.lastIndex downTo 0) {
            val ball = balls[i] as? Ball ?: continue

            for (j in blocks.lastIndex downTo 0) {
                val block = blocks[j] as? Block ?: continue

                if (RectF.intersects(ball.boundingBox, block.boundingBox)) {

                    val overlapX = Math.min(ball.boundingBox.right, block.boundingBox.right) - Math.max(ball.boundingBox.left, block.boundingBox.left)
                    val overlapY = Math.min(ball.boundingBox.bottom, block.boundingBox.bottom) - Math.max(ball.boundingBox.top, block.boundingBox.top)

                    if (overlapX < overlapY) {
                        ball.dx = -ball.dx
                        if (ball.x < block.x + block.width / 2) ball.x -= overlapX else ball.x += overlapX
                    } else {
                        if (ball.dy > 0) {
                            ball.dy = ball.bounceY
                            ball.y -= overlapY
                        } else {
                            ball.dy = -ball.dy
                            ball.y += overlapY
                        }
                    }
                }
            }
        }
    }

    private fun checkPlayerBallCollision(gctx: GameContext) {
        val scene = gctx.scene as? MainScene ?: return
        val player = scene.player
        val balls = world.objectsAt(MainLayer.BALL)

        for (i in balls.lastIndex downTo 0) {
            val ball = balls[i] as? Ball ?: continue

            if (RectF.intersects(player.boundingBox, ball.boundingBox)) {
                scene.onPlayerHit()
                break
            }
        }
    }

    override fun draw(canvas: Canvas) {
    }
}