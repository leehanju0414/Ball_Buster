package com.example.ball_buster.scene

import android.graphics.Canvas
import android.view.MotionEvent
import com.example.ball_buster.objects.Player
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.R
import com.example.ball_buster.objects.Ball
import com.example.ball_buster.objects.Block
import com.example.ball_buster.objects.CollisionChecker
import com.example.ball_buster.objects.ScoreBoard

class MainScene(gctx: GameContext) : Scene(gctx) {
    val joystick = JoyStick(gctx, R.drawable.joy_bg, R.drawable.joy_thumb, 300f, 700f, 150f, 50f)
    val player = Player(gctx, joystick)
    val scoreBoard = ScoreBoard()
    val MAX_STAGE = 5
    var isGameOver = false
    var isGameClear = false

    override val world = World(MainLayer.entries.toTypedArray()).apply {
        add(player, MainLayer.PLAYER)
        add(joystick, MainLayer.UI)
        add(Ball(800f, 200f, 300f), MainLayer.BALL)
        add(Ball(800f, 100f, 300f), MainLayer.BALL)
        add(CollisionChecker(this), MainLayer.UI)
        add(scoreBoard, MainLayer.UI)
    }

    init{
        loadStage(scoreBoard.stage)
    }

    private fun loadStage(stage: Int) {
        world.objectsAt(MainLayer.BALL).toList().forEach { world.remove(it, MainLayer.BALL) }
        world.objectsAt(MainLayer.HARPOON).toList().forEach { world.remove(it, MainLayer.HARPOON) }
        world.objectsAt(MainLayer.BLOCK).toList().forEach { world.remove(it, MainLayer.BLOCK) }

        player.x = gctx.metrics.width / 2f

        when (stage) {
            1 -> {
                world.add(Ball(800f, 100f, 300f, level = 3), MainLayer.BALL)
            }
            2 -> {
                //중앙을 가로막는 블록
                world.add(Ball(400f, 100f, -300f, level = 3), MainLayer.BALL)
                world.add(Ball(1200f, 100f, 300f, level = 3), MainLayer.BALL)
                world.add(Block(650f, 400f, 300f, 50f), MainLayer.BLOCK)
            }
            3 -> {
                //양옆에 블록
                world.add(Ball(200f, 100f, 250f, level = 3), MainLayer.BALL)
                world.add(Ball(1400f, 100f, -250f, level = 3), MainLayer.BALL)

                world.add(Block(150f, 500f, 250f, 50f), MainLayer.BLOCK) // 좌측 타워
                world.add(Block(1200f, 500f, 250f, 50f), MainLayer.BLOCK) // 우측 타워
            }
            4 -> {
                //천장 중앙에 거대한 블록
                world.add(Ball(400f, 150f, 350f, level = 3), MainLayer.BALL)
                world.add(Ball(1200f, 150f, -350f, level = 3), MainLayer.BALL)

                world.add(Block(350f, 550f, 900f, 60f), MainLayer.BLOCK)
            }
            5 -> {
                //복잡한 블록 지형
                world.add(Ball(800f, 50f, 300f, level = 3), MainLayer.BALL)
                world.add(Ball(300f, 100f, 250f, level = 3), MainLayer.BALL)
                world.add(Ball(1300f, 100f, -250f, level = 3), MainLayer.BALL)

                world.add(Block(200f, 450f, 300f, 50f), MainLayer.BLOCK) // 좌측 중단
                world.add(Block(1100f, 450f, 300f, 50f), MainLayer.BLOCK) // 우측 중단
                world.add(Block(650f, 250f, 300f, 50f), MainLayer.BLOCK)  // 중앙 상단
            }
            else -> {
                android.util.Log.e("BallBuster", "존재하지 않는 스테이지 호출: $stage")
            }
        }
    }

    fun onPlayerHit() {
        scoreBoard.lives--
        if (scoreBoard.lives > 0) {
            player.isInvincible = true
            player.invincibleTimer = player.invincibleDuration
        } else {
            scoreBoard.lives = 0
            isGameOver = true
        }
    }

    override fun update(gctx: GameContext) {
        if (isGameOver || isGameClear) return

        super.update(gctx)

        if (scoreBoard.stage > MAX_STAGE || scoreBoard.lives <= 0) return

        //화면에 공이 하나도 없을 때
        if (world.countAt(MainLayer.BALL) == 0) {
            if (scoreBoard.stage < MAX_STAGE) {
                //다음 스테이지가 있으면 정상 진행
                scoreBoard.stage++
                loadStage(scoreBoard.stage)
            } else {
                //최종 스테이지를 클리어했을 때
                scoreBoard.stage = MAX_STAGE + 1
                isGameClear = true
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pt = gctx.metrics.fromScreen(event.x, event.y)
        val isRightSide = pt.x > gctx.metrics.width / 2

        val isActionDown = event.actionMasked == MotionEvent.ACTION_DOWN ||
                event.actionMasked == MotionEvent.ACTION_POINTER_DOWN

        if (isRightSide) {
            if (isActionDown) {
                player.fire()
            }
        } else {
            joystick.onTouchEvent(event)
        }

        if (isGameOver || isGameClear) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                isGameOver = false
                isGameClear = false
                scoreBoard.lives = 3
                scoreBoard.score = 0
                scoreBoard.stage = 1
                loadStage(1)
            }
            return true
        }

        return true
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (isGameOver) {
            val paint = android.graphics.Paint()

            paint.color = android.graphics.Color.argb(180, 0, 0, 0)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, paint)

            paint.color = android.graphics.Color.RED
            paint.textSize = 120f
            paint.textAlign = android.graphics.Paint.Align.CENTER
            paint.isFakeBoldText = true
            canvas.drawText("GAME OVER", gctx.metrics.width / 2f, gctx.metrics.height / 2f - 50f, paint)

            paint.color = android.graphics.Color.WHITE
            paint.textSize = 50f
            paint.isFakeBoldText = false
            canvas.drawText("Final Score: ${scoreBoard.score}", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 50f, paint)
            canvas.drawText("Tap to Restart", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 150f, paint)
        }
        else if (isGameClear) {
            val paint = android.graphics.Paint()

            paint.color = android.graphics.Color.argb(150, 255, 255, 255)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, paint)

            paint.color = android.graphics.Color.rgb(255, 215, 0) // Gold 색상
            paint.textSize = 120f
            paint.textAlign = android.graphics.Paint.Align.CENTER
            paint.isFakeBoldText = true
            canvas.drawText("STAGE CLEAR!", gctx.metrics.width / 2f, gctx.metrics.height / 2f - 50f, paint)

            paint.color = android.graphics.Color.BLACK
            paint.textSize = 50f
            paint.isFakeBoldText = false
            canvas.drawText("Final Score: ${scoreBoard.score}", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 50f, paint)
            canvas.drawText("Tap to Restart", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 150f, paint)
        }
    }
}