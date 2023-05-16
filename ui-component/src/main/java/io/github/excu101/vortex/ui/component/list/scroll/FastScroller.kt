package io.github.excu101.vortex.ui.component.list.scroll

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.getSize
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.accentColorKey
import kotlin.math.ceil

class FastScroller(context: Context?) : View(context) {

    private val animator = AnimatorSet()

    private val autoHide = Runnable {

    }

    private val autoShow = Runnable {

    }

    private val handle = RectF()
    private var dy: Float = 0F
    private val progress: Float = 0F

    private val handlePaint = Paint().apply {
        color = ThemeColor(accentColorKey)
    }

    private var recycler: RecyclerView? = null

    private val listener = Scroller()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                invalidate()
                true
            }
            MotionEvent.ACTION_MOVE -> {
                invalidate()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(24.dp, getSize(heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val y = paddingTop + ceil(measuredHeight - paddingTop - 56.dp * progress)
        canvas.drawRoundRect(handle, 2F.dp, 2F.dp, handlePaint)
    }

    fun bindRecycler(recycler: RecyclerView?) {
        this.recycler = recycler
        recycler?.addOnScrollListener(listener)
    }

    private inner class Scroller : RecyclerView.OnScrollListener() {
        override fun onScrolled(recycler: RecyclerView, dx: Int, dy: Int) {
            this@FastScroller.dy = (measuredHeight - dy).toFloat()
            invalidate()
        }
    }


}