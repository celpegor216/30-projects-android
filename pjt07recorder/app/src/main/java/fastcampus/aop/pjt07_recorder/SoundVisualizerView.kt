package fastcampus.aop.pjt07_recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SoundVisualizerView(context: Context, attrs: AttributeSet): View(context, attrs) {

    // MainActivity에서 정의하여,
    // 함수 호출 시 MainActivity에서 사용하는 recorder의 maxAmplitude 값을 반환함
    var onRequestCurrentAmplitude: (() -> Int)? = null

    // ANTI_ALIAS_FLAG: 계단화 방지, 곡선이 부드럽게 그려짐
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH

        // 선의 양 끝 모양 지정
        strokeCap = Paint.Cap.ROUND
    }
    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var isReplaying = false
    private var replayingPosition = 0

    private val visualizeRepeatAction = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                // 새로운 데이터 추가
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            // onDraw() 호출
            invalidate()

            // 20초마다 반복 수행
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawingWidth = w
        drawingHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas ?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        drawingAmplitudes.let{ amplitudes ->
            if (isReplaying) {
                amplitudes.takeLast(replayingPosition)
            } else {
                amplitudes
            }
        }.forEach { amplitude ->
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F

            // 화면을 벗어나는 항목은 그리지 않음
            offsetX -= LINE_SPACE
            if (offsetX < 0) return@forEach

            canvas.drawLine(offsetX, centerY - lineLength / 2F, offsetX, centerY + lineLength / 2F, amplitudePaint)
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun clearVisualization() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }
}