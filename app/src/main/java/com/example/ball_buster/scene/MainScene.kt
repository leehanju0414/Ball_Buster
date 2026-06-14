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
import com.example.ball_buster.objects.SoundManager

class MainScene(gctx: GameContext) : Scene(gctx) {
    val joystick = JoyStick(gctx, R.drawable.joy_bg, R.drawable.joy_thumb, 300f, 700f, 150f, 50f)
    val player = Player(gctx, joystick)
    val scoreBoard = ScoreBoard()
    val MAX_STAGE = 5
    var isGameOver = false
    var isGameClear = false
    var isStageTransition = false
    var transitionTimer = 0f
    var isPaused = false
    val pauseButtonRect = android.graphics.RectF(1450f, 10f, 1570f, 130f)
    val titleButtonRect = android.graphics.RectF(600f, 650f, 1000f, 750f)
    var isJoystickGrabbed = false

    private val gameArea by lazy { android.graphics.RectF(0f, 0f, 1600f, 900f) }
    private val bgPaint by lazy { android.graphics.Paint() }
    private val uiPaint by lazy { android.graphics.Paint() }

    override val world = World(MainLayer.entries.toTypedArray()).apply {
        add(player, MainLayer.PLAYER)
        add(joystick, MainLayer.UI)
        add(Ball(800f, 200f, 300f), MainLayer.BALL)
        add(Ball(800f, 100f, 300f), MainLayer.BALL)
        add(CollisionChecker(this), MainLayer.UI)
        add(scoreBoard, MainLayer.UI)
    }

    init{
        SoundManager.stopBgm()
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
            SoundManager.playSfx(SoundManager.sfxHit)
        } else {
            scoreBoard.lives = 0
            isGameOver = true
        }
    }

    override fun update(gctx: GameContext) {
        if (isPaused || isGameOver || isGameClear) return

        if (isStageTransition) {
            transitionTimer -= gctx.frameTime
            if (transitionTimer <= 0f) {
                isStageTransition = false
                scoreBoard.stage++
                loadStage(scoreBoard.stage)
            }
            return
        }

        super.update(gctx)

        if (scoreBoard.stage > MAX_STAGE || scoreBoard.lives <= 0) return

        //화면에 공이 하나도 없을 때
        if (world.countAt(MainLayer.BALL) == 0) {
            if (scoreBoard.stage < MAX_STAGE) {
                //멈췄다가 다음스테이지로 이동
                isStageTransition = true
                transitionTimer = 2.0f
            } else {
                //최종 스테이지를 클리어했을 때
                scoreBoard.stage = MAX_STAGE + 1
                isGameClear = true
                SoundManager.playSfx(SoundManager.sfxClear)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val pt = gctx.metrics.fromScreen(event.x, event.y)
        val touchX = pt.x
        val touchY = pt.y

        if (isGameOver || isGameClear) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (titleButtonRect.contains(touchX, touchY)) {
                    com.example.ball_buster.activity.GameActivity.instance?.finish()
                    return true
                }

                if(isGameOver) {
                    isGameOver = false
                    scoreBoard.lives = 3
                    scoreBoard.score = 0
                    scoreBoard.stage = 1
                    loadStage(1)
                }
            }
            return true
        }

        //일시정지 해제 및 일시정지 버튼 터치 처리
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isPaused) {
                if (titleButtonRect.contains(touchX, touchY)) {
                    isPaused = false
                    com.example.ball_buster.activity.GameActivity.instance?.finish()
                    return true
                }
                isPaused = false
                return true
            }

            if (!isStageTransition && pauseButtonRect.contains(touchX, touchY)) {
                isPaused = true
                return true
            }
        }

        if (isPaused || isStageTransition) return true

        val action = event.actionMasked
        val pointerIndex = event.actionIndex
        val ptAction = gctx.metrics.fromScreen(event.getX(pointerIndex), event.getY(pointerIndex))
        val isRightSide = ptAction.x > gctx.metrics.width / 2

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (isRightSide) {
                    player.fire()
                } else {
                    isJoystickGrabbed = true
                    joystick.onTouchEvent(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isJoystickGrabbed) {
                    joystick.onTouchEvent(event)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                if (isJoystickGrabbed) {
                    joystick.onTouchEvent(event)

                    if (event.pointerCount <= 1) {
                        isJoystickGrabbed = false
                    }
                }
            }
        }

        return true
    }


    override fun draw(canvas: Canvas) {
        canvas.drawColor(android.graphics.Color.BLACK)

        bgPaint.style = android.graphics.Paint.Style.FILL
        when (scoreBoard.stage) {
            1 -> bgPaint.color = android.graphics.Color.rgb(20, 30, 40)
            2 -> bgPaint.color = android.graphics.Color.rgb(40, 20, 20)
            3 -> bgPaint.color = android.graphics.Color.rgb(20, 40, 20)
            4 -> bgPaint.color = android.graphics.Color.rgb(40, 30, 10)
            5 -> bgPaint.color = android.graphics.Color.rgb(30, 10, 40)
            else -> bgPaint.color = android.graphics.Color.rgb(20, 30, 40)
        }
        canvas.drawRect(gameArea, bgPaint)

        bgPaint.style = android.graphics.Paint.Style.STROKE
        bgPaint.strokeWidth = 6f
        bgPaint.color = android.graphics.Color.WHITE
        canvas.drawRect(gameArea, bgPaint)

        super.draw(canvas)
        
        if (!isGameOver && !isGameClear) {
            uiPaint.textSize = 70f
            uiPaint.isFakeBoldText = true
            uiPaint.textAlign = android.graphics.Paint.Align.CENTER

            uiPaint.style = android.graphics.Paint.Style.STROKE
            uiPaint.strokeWidth = 10f
            uiPaint.color = android.graphics.Color.BLACK
            canvas.drawText("| |", 1510f, 95f, uiPaint)

            uiPaint.style = android.graphics.Paint.Style.FILL
            uiPaint.strokeWidth = 0f
            uiPaint.color = android.graphics.Color.WHITE
            canvas.drawText("| |", 1510f, 95f, uiPaint)
        }

        if (isPaused) {
            uiPaint.color = android.graphics.Color.argb(150, 0, 0, 0)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, uiPaint)

            uiPaint.color = android.graphics.Color.WHITE
            uiPaint.textSize = 150f
            uiPaint.textAlign = android.graphics.Paint.Align.CENTER
            canvas.drawText("PAUSED", gctx.metrics.width / 2f, gctx.metrics.height / 2f, uiPaint)

            uiPaint.textSize = 50f
            uiPaint.isFakeBoldText = false
            canvas.drawText("Tap anywhere to Resume", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 100f, uiPaint)

            drawTitleButton(canvas, uiPaint)
        }
        else if (isGameOver) {
            uiPaint.color = android.graphics.Color.argb(180, 0, 0, 0)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, uiPaint)

            uiPaint.color = android.graphics.Color.RED
            uiPaint.textSize = 120f
            uiPaint.textAlign = android.graphics.Paint.Align.CENTER
            uiPaint.isFakeBoldText = true
            canvas.drawText("GAME OVER", gctx.metrics.width / 2f, gctx.metrics.height / 2f - 50f, uiPaint)

            uiPaint.color = android.graphics.Color.WHITE
            uiPaint.textSize = 50f
            uiPaint.isFakeBoldText = false
            canvas.drawText("Final Score: ${scoreBoard.score}", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 50f, uiPaint)
            canvas.drawText("Tap to Restart", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 150f, uiPaint)

            drawTitleButton(canvas, uiPaint)
        }
        else if (isStageTransition) {
            uiPaint.color = android.graphics.Color.argb(120, 0, 0, 0)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, uiPaint)

            uiPaint.color = android.graphics.Color.YELLOW
            uiPaint.textSize = 100f
            uiPaint.textAlign = android.graphics.Paint.Align.CENTER
            uiPaint.isFakeBoldText = true
            canvas.drawText("STAGE ${scoreBoard.stage} CLEAR!", gctx.metrics.width / 2f, gctx.metrics.height / 2f - 30f, uiPaint)

            uiPaint.color = android.graphics.Color.WHITE
            uiPaint.textSize = 60f
            canvas.drawText("Get Ready...", gctx.metrics.width / 2f, gctx.metrics.height / 2f + 80f, uiPaint)
        }
        else if (isGameClear) {
            uiPaint.color = android.graphics.Color.argb(150, 255, 255, 255)
            canvas.drawRect(0f, 0f, gctx.metrics.width, gctx.metrics.height, uiPaint)

            uiPaint.color = android.graphics.Color.rgb(255, 215, 0) // Gold 색상
            uiPaint.textSize = 120f
            uiPaint.textAlign = android.graphics.Paint.Align.CENTER
            uiPaint.isFakeBoldText = true
            canvas.drawText("STAGE CLEAR!", gctx.metrics.width / 2f, gctx.metrics.height / 2f - 50f, uiPaint)

            drawTitleButton(canvas, uiPaint)
        }
    }

    private fun drawTitleButton(canvas: Canvas, paint: android.graphics.Paint) {
        paint.style = android.graphics.Paint.Style.FILL
        paint.color = android.graphics.Color.DKGRAY
        canvas.drawRect(titleButtonRect, paint)

        paint.style = android.graphics.Paint.Style.STROKE
        paint.strokeWidth = 6f
        paint.color = android.graphics.Color.WHITE
        canvas.drawRect(titleButtonRect, paint)

        paint.style = android.graphics.Paint.Style.FILL
        paint.strokeWidth = 0f
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 45f
        paint.isFakeBoldText = true
        canvas.drawText("BACK TO TITLE", titleButtonRect.centerX(), titleButtonRect.centerY() + 15f, paint)
    }
}