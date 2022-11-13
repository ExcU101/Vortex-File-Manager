package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updatePadding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

open class BottomDrawerLayout(
    context: Context,
) : CoordinatorLayout(
    context
), Drawer {

    private val behavior = BottomSheetBehavior<View>().apply {
        skipCollapsed = true
        isHideable = true
    }

    private val background = MaterialShapeDrawable(
        builder()
            .setTopRightCorner(CornerFamily.ROUNDED, 16F.dp)
            .setTopLeftCorner(CornerFamily.ROUNDED, 16F.dp)
            .build()
    ).apply {
        tintList = ColorStateList.valueOf(ThemeColor(mainDrawerBackgroundColorKey))
    }

    private var contentIndex = childCount

    private var onSlide: ((View, Float) -> Unit)? = null

    private val callback = object : BottomSheetCallback() {
        override fun onStateChanged(sheet: View, state: Int) {

        }

        override fun onSlide(sheet: View, offset: Float) {
            background.interpolation = 1F - offset
            scrim.alpha = offset
            scrim.visibility = if (offset <= 0) {
                GONE
            } else {
                VISIBLE
            }
            onSlide?.invoke(sheet, offset)
        }
    }

    private val scrim = View(context).apply {
        alpha = 0F
        visibility = GONE

        isClickable = true
        isFocusable = true

        setBackgroundColor(Color(0x52000000).value)

        setOnClickListener { view ->
            hide()
        }
    }

    private val container = FrameLayout(context).apply {
        background = this@BottomDrawerLayout.background
    }

    init {
        behavior.addBottomSheetCallback(callback)
    }

    override val isOpen: Boolean
        get() = behavior.state == STATE_EXPANDED || behavior.state == STATE_COLLAPSED

    override val isClosed: Boolean
        get() = behavior.state == STATE_HIDDEN

    override fun show() {
        behavior.state = STATE_EXPANDED
//        behavior.state = STATE_COLLAPSED
    }

    override fun hide() {
        behavior.state = STATE_HIDDEN
    }

    fun bindOnSlideCallback(onSlide: (View, Float) -> Unit) {
        this.onSlide = onSlide
    }

    override fun toggle() {
        if (isOpen) hide() else show()
    }

    fun replaceContent(
        content: View,
        vararg contentAbove: Pair<View, LayoutParams?> = arrayOf(),
    ) {
        wrapContent(content, *contentAbove)
    }

    fun replaceContainerView(view: View) {
        container.removeAllViews()
        container.addView(view)
    }

    fun updateContainerPaddings(
        @Px left: Int = 0,
        @Px top: Int = 0,
        @Px right: Int = 0,
        @Px bottom: Int = 0,
    ) {
        container.updatePadding(
            left = left,
            top = top,
            right = right,
            bottom = bottom
        )
    }

    private fun wrapContent(
        content: View,
        vararg contentAbove: Pair<View, LayoutParams?> = arrayOf(),
    ) {
        addView(content, contentIndex)
        addView(scrim)
        addView(container, LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            behavior = this@BottomDrawerLayout.behavior
        })

        contentAbove.forEach { (view, params) ->
            addView(view, params)
        }
    }

}