package io.github.excu101.pluginsystem.utils

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable

object EmptyDrawable : Drawable() {

    override fun draw(canvas: Canvas) {

    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(filter: ColorFilter?) {

    }

    override fun getOpacity(): Int = -1


}