package io.github.excu101.vortex.ui.view.menu

import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import io.github.excu101.vortex.ui.view.dp
import kotlin.math.abs

class MenuDrawable : Drawable() {

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 2F.dp
    }

    @FloatRange(from = 0.0, to = 1.0)
    var rotation: Float = 0F

    override fun draw(canvas: Canvas): Unit = with(canvas) {
        val start = bounds.left
        val end = bounds.right

        val startX = start + 4.dp
        val endX = end - 4.dp

        drawLine(
            startX.toFloat(),
            0F,
            endX.toFloat(),
            0F,
            paint
        )
        val diffUp = 9.dp * (1.0f - 1.0F)
        val endYDiff = 5.dp * (1 - abs(rotation)) - 0.5f.dp * abs(rotation)
        val endXDiff = 18.dp - 9.dp * abs(rotation)
        val startYDiff = 5.dp + 3.0f.dp * abs(rotation)
        val startXDiff = 9.dp * abs(rotation)

//        canvas.drawLine(startXDiff, -startYDiff, endXDiff - diffUp, -endYDiff, paint);
//        canvas.drawLine(startXDiff, startYDiff, endXDiff, endYDiff, paint);
    }

    override fun setAlpha(alpha: Int) {
        if (alpha != paint.alpha) {
            paint.alpha = alpha
            invalidateSelf()
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        if (paint.colorFilter != colorFilter) {
            paint.colorFilter = colorFilter
            invalidateSelf()
        }
    }

    override fun getIntrinsicWidth(): Int {
        return 24.dp
    }

    override fun getIntrinsicHeight(): Int {
        return 24.dp
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

}