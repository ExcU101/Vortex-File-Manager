package io.github.excu101.vortex.ui.component

import android.view.View
import androidx.viewpager.widget.ViewPager

class PageTransformer : ViewPager.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            alpha = when {
                position < -1 -> {
                    0f
                }

                position <= 0 -> {
                    1f
                }

                position <= 1 -> {
                    1 - position
                }

                else -> {
                    0f
                }
            }
        }

    }
}