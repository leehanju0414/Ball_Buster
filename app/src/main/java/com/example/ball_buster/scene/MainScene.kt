package com.example.ball_buster.scene

import android.view.MotionEvent
import com.example.ball_buster.objects.Player
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import com.example.ball_buster.R
import com.example.ball_buster.objects.Ball

class MainScene(gctx: GameContext) : Scene(gctx) {
    val joystick = JoyStick(gctx, R.drawable.joy_bg, R.drawable.joy_thumb, 300f, 700f, 150f, 50f)

    val player = Player(gctx, joystick)

    override val world = World(MainLayer.entries.toTypedArray()).apply {
        add(player, MainLayer.PLAYER)
        add(joystick, MainLayer.UI)
        add(Ball(gctx, 800f, 200f, 300f), MainLayer.BALL)
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