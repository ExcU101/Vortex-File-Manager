package io.github.excu101.vortex.ui.component.loading

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.Gravity
import androidx.annotation.FloatRange
import com.google.android.material.button.MaterialButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.vortex.theme.key.storageListLoadingHeightKey
import io.github.excu101.vortex.theme.key.storageListLoadingWidthKey
import io.github.excu101.vortex.theme.model.Color
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.sp
import io.github.excu101.vortex.ui.component.themeMeasure

class FileProgressView(context: Context) : ThemeFrameLayout(context), FileProgressController {

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF0062EE.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 1.5F.dp
    }

    private val trackBounds = RectF()

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LightGray.value
        style = Paint.Style.STROKE
        strokeWidth = 1F.dp
    }

    private var indicatorAnimator: ValueAnimator? = null

    private val unitPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.Black.value
        textSize = 12F.sp
    }

    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.Black.value
        style = Paint.Style.FILL
        textSize = 16F.sp
    }

    @FloatRange(from = 0.0, to = 1.0)
    override var progress: Float = 0F
    override var unit: String? = null
    override var title: String? = null

    fun updateProgress(progress: Float) {
        if (this.progress == progress) return
        if (this.progress == 1F) return
        if (progress > 1.0F) {
            this.progress = 1F
        }
        createIndicatorAnimator(progress)?.start()
    }

    fun updateProgress(new: (Float) -> Float) {
        updateProgress(new(this.progress))
    }

    private fun getUnitWidth(): Int {
        return if (unit.isNullOrEmpty()) 0 else unit!!.length.dp
    }

    private fun getTitleWidth(): Int {
        return if (title.isNullOrEmpty()) 0 else title!!.length.dp
    }

    override fun onColorChanged() {

    }

    init {
        background = MaterialShapeDrawable(
            builder().setAllCornerSizes(16F.dp).build()
        ).apply {
            this.fillColor = ColorStateList.valueOf(0xFFFFFFFF.toInt())
        }

        unit = "3KB"
        title = "432432"

        addView(
            MaterialButton(
                context,
                null,
                com.google.android.material.R.attr.borderlessButtonStyle
            ).apply {
                text = "432423"
                cornerRadius = 100
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 8.dp
                    gravity = Gravity.CENTER or Gravity.END
                }
            }
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        themeMeasure(
            widthMeasureSpec,
            heightMeasureSpec,
            storageListLoadingWidthKey,
            storageListLoadingHeightKey,
            ::setMeasuredDimension
        )
    }

    override fun onDraw(canvas: Canvas) {
        val radius = 16F.dp

        trackBounds.set(
            16F.dp,
            8F.dp,
            16F.dp + (radius * 2),
            height - 8F.dp,
        )

//        if (!isVisible) {
        canvas.drawArc(
            trackBounds,
            0F,
            360F,
            false,
            trackPaint
        )

        canvas.drawArc(
            trackBounds,
            0F,
            progress * 360F,
            false,
            indicatorPaint
        )

        if (!unit.isNullOrEmpty()) {
            canvas.drawText(
                unit!!,
                trackBounds.centerX(),
                trackBounds.bottom - trackBounds.top + unitPaint.fontMetrics.bottom - unitPaint.fontMetrics.top,
                unitPaint
            )
        }

        if (!title.isNullOrEmpty()) {
            canvas.drawText(
                title!!,
                trackBounds.right + 16F.dp,
                trackBounds.top + titlePaint.fontMetrics.bottom - titlePaint.fontMetrics.top,
                titlePaint
            )
        }
//        }
    }


    private fun createIndicatorAnimator(to: Float): ValueAnimator? {
        if (indicatorAnimator != null) {
            indicatorAnimator!!.cancel()
        }
        indicatorAnimator = ValueAnimator.ofFloat(progress, to)
        indicatorAnimator!!.addUpdateListener { animator ->
            progress = animator.animatedValue as Float
            invalidate()
        }
        indicatorAnimator!!.duration = 500L
        indicatorAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationCancel(animation: Animator) {
                progress = to
                indicatorAnimator = null
            }

            override fun onAnimationEnd(animation: Animator) {
                progress = to
                indicatorAnimator = null
            }
        })

        return indicatorAnimator
    }

    override fun show(bar: Bar?) {
        animate().setDuration(100L).translationY(0F)
    }

    override fun hide(bar: Bar?) {
        animate().setDuration(100L).translationY((bar?.height ?: 0) + height.toFloat())
    }
}