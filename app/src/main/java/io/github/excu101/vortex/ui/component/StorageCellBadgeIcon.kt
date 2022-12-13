package io.github.excu101.vortex.ui.component

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Paint.*
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import androidx.core.graphics.drawable.toBitmap
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconSelectedTintColorKey
import io.github.excu101.vortex.ui.icon.Icons

class StorageCellBadgeIcon(
    private val iconWidth: Int,
    private val iconHeight: Int,
) : Drawable() {

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = ThemeColor(storageListItemIconSelectedTintColorKey)
    }

    private val paint = Paint(ANTI_ALIAS_FLAG or DITHER_FLAG or FILTER_BITMAP_FLAG)

    var duration: Long
        get() = animator.duration
        set(value) {
            animator.duration = value
        }

    @FloatRange(from = 0.0, to = 1.0)
    private var progress: Float = 0F
        set(value) {
            field = value
            invalidateSelf()
        }

    private val animator = AnimatorSet().apply {
        play(ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener { anim ->
                progress = anim.animatedFraction
            }
        })
    }

    var isSelected: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            animator.start()
        }

    var isBookmarked: Boolean = false
        set(value) {
            if (field == value) return
            field = value
            animator.start()
        }

    override fun draw(canvas: Canvas) {

        if (isBookmarked) {
            val x = iconWidth / 2
            val y = iconHeight / 2

            Icons.Rounded.Bookmark?.toBitmap(
                x,
                y,
            )?.let { icon ->
                canvas.drawBitmap(
                    icon,
                    0F,
                    0F,
                    null
                )
            }
        }
        if (isSelected) {
            val x = iconWidth / 2
            val y = iconHeight / 2
            canvas.drawCircle(
                x.toFloat() + y / 4, y.toFloat() + y / 4, 8F.dp, backgroundPaint
            )
            Icons.Rounded.Check?.toBitmap(
                x / 2,
                y / 2,
            )?.let { icon ->
                canvas.drawBitmap(
                    icon,
                    x.toFloat() / 2 + x / 2,
                    y.toFloat() / 2 + y / 2,
                    paint
                )
            }
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getAlpha(): Int {
        return paint.alpha
    }

    override fun setColorFilter(filter: ColorFilter?) {
        paint.colorFilter = filter
    }

    override fun getColorFilter(): ColorFilter? {
        return paint.colorFilter
    }

    fun setBackgroundColorFilter(filter: ColorFilter?) {
        backgroundPaint.colorFilter = filter
    }

    fun getBackgroundColorFilter(): ColorFilter? {
        return backgroundPaint.colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }
}