package com.example.ball_buster.scene

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
                world.add(Ball(400f, 100f, -300f, level = 3), MainLayer.BALL)
                world.add(Ball(1200f, 100f, 300f, level = 3), MainLayer.BALL)
                world.add(Block(650f, 400f, 300f, 50f), MainLayer.BLOCK) // 가운데 블록
            }
            else -> {
                // 여기다가 클리어신으로 넘어가게
            }
        }
    }

    fun onPlayerHit() {
        scoreBoard.lives--
        if (scoreBoard.lives > 0) {
            // 맞았다고 깜박거리는 애니메이션을 넣으면 좋을듯
        } else {
            // 게임오버 신으로 전환
        }
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        // 공 다 제거하면 다음 스테이지로 이동
        if (world.countAt(MainLayer.BALL) == 0) {
            scoreBoard.stage++
            loadStage(scoreBoard.stage)
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

        return true
    }
}