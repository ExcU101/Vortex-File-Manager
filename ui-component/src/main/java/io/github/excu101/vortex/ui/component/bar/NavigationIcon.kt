package io.github.excu101.vortex.ui.component.bar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import io.github.excu101.vortex.ui.component.bar.NavigationIcon.Type.BACK
import io.github.excu101.vortex.ui.component.bar.NavigationIcon.Type.CLOSE
import io.github.excu101.vortex.ui.component.dp
import kotlin.math.roundToInt

/**
 * Represent `Hamburger` icon, which animates to different type (e.g `back`, `close`)
 **/

class NavigationIcon : Drawable {

    private val iconWidth = 24.dp
    private val iconHeight = 24.dp
    private val lineHorizontalGap = 3.dp

    private val horizontalPadding
        get() = leftPadding + rightPadding

    private val leftPadding = 3.dp
    private val rightPadding = 3.dp

    private val lineThickness = 2.dp

    private var animator: Animator? = null

    private val clearAnimatorListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            animator?.removeListener(this)
            animator = null
        }

        override fun onAnimationCancel(animation: Animator) {
            animator?.removeListener(this)
            animator = null
        }
    }

    private val path = Path()

    var type: Type = BACK

    val isMenu: Boolean
        get() = progress.roundToInt() == 1

    enum class Type {
        CLOSE,
        BACK
    }

    constructor() {

    }

    constructor(type: Type) : this() {
        this.type = type
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = lineThickness.toFloat()
        color = Color.MAGENTA
        style = Paint.Style.STROKE
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    @FloatRange(from = 0.0, to = 1.0)
    var progress: Float = 0.0f
        set(value) {
            field = value
            rotation = value * 180
            invalidateSelf()
        }

    private var rotation = 0F

    override fun draw(canvas: Canvas) {
        path.rewind()
        createPathTopLine()    // -
        createPathMiddleLine() // -
        createPathBottomLine() // -
        path.close()
        canvas.save()

        canvas.rotate(rotation, centerX().toFloat(), centerY().toFloat())
        canvas.drawPath(path, paint)

        canvas.restore()
    }

    private fun createPathTopLine() = with(path) {
        var startX: Float
        var endX: Float
        var startY = (centerY().toFloat()) - lineHorizontalGap - lineThickness
        var endY = startY

        when (type) {
            CLOSE -> {
                startX = leftPadding.toFloat()
                endX = iconWidth - rightPadding.toFloat()
            }
            BACK -> {
                startX = leftPadding.toFloat()
                endX = iconWidth - rightPadding.toFloat()
            }
        }

        moveTo(startX, startY)
        lineTo(endX, endY)
    }

    private fun createPathMiddleLine() = with(path) {
        var startX: Float
        var endX: Float
        var startY = centerY().toFloat()
        var endY = startY

        when (type) {
            CLOSE -> {
                startX = leftPadding.toFloat() + (9.dp * progress)
                endX = iconWidth - rightPadding.toFloat() - (9.dp * progress)
            }
            BACK -> {
                startX = leftPadding.toFloat()
                endX = iconWidth - rightPadding.toFloat()
            }
        }

        moveTo(startX, startY)
        lineTo(endX, endY)
    }

    private fun createPathBottomLine() = with(path) {
        var startX: Float
        var endX: Float
        val offsetY: Float = (lineHorizontalGap + lineThickness).toFloat()
        var startY = centerY().toFloat() + offsetY
        var endY = startY

        when (type) {
            CLOSE -> {
                startX = leftPadding.toFloat()
                endX = iconWidth - rightPadding.toFloat()
            }
            BACK -> {
                startX = leftPadding.toFloat()
                endX = iconWidth - rightPadding.toFloat()
            }
        }

        moveTo(startX, startY)
        lineTo(endX, endY)
    }

    override fun setAlpha(alpha: Int) {
        if (paint.alpha == alpha) return

        paint.alpha = alpha
        invalidateSelf()
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (paint.colorFilter == colorFilter) return

        paint.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    private fun reverseProgress(): Float {
        return 1F - progress
    }

    private fun centerX(): Int {
        return intrinsicWidth / 2
    }

    private fun centerY(): Int {
        return intrinsicHeight / 2
    }

    fun animateTo(type: Type) {
        this.type = type
        animator = ValueAnimator.ofFloat(progress, 1F).apply {
            addUpdateListener { animator ->
                progress = animator.animatedValue as Float
            }
        }
        animator?.addListener(clearAnimatorListener)
        animator?.start()
    }

    fun clearAnimation() {
        if (animator != null) {
            animator?.cancel()
            animator = null
        }
    }

    fun getWidthWithLeftPadding(): Int {
        return horizontalPadding
    }

    override fun getIntrinsicHeight(): Int {
        return iconHeight
    }

    override fun getIntrinsicWidth(): Int {
        return iconWidth
    }

    private fun lerp(a: Float, b: Float, c: Float): Float {
        return a + (b - a) * c
    }
}