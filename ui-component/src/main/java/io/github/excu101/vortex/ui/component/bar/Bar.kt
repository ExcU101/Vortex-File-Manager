package io.github.excu101.vortex.ui.component.bar

import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.*
import kotlin.math.min

class Bar(context: Context) : FrameLayout(context) {

    enum class Type {
        TOP,
        BOTTOM
    }

    var type: Type = Type.BOTTOM
        set(value) {
            field = value
            invalidate()
        }

    private val horizontalPadding = 16.dp
    private val verticalPadding = 8.dp
    private val titleHorizontalPadding = 32.dp

    private val background = MaterialShapeDrawable().apply {
        initializeElevationOverlay(context)
        shadowCompatibilityMode = SHADOW_COMPAT_MODE_ALWAYS
        fillColor = valueOf(ThemeColor(key = mainBarSurfaceColorKey))
    }

    private val navigationIconView = ImageView(context).apply {
        isClickable = true
        isFocusable = true
        background =
            RippleDrawable(valueOf(ThemeColor(mainBarNavigationIconTintColorKey)), null, null)
        setColorFilter(ThemeColor(mainBarNavigationIconTintColorKey))
    }

    private val titleView = TextView(context).apply {
        textSize = 18F
        setTextColor(ThemeColor(mainBarTitleTextColorKey))
    }

    private val subtitleView = TextView(context).apply {
        textSize = 14F
        setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
    }

    var navigationIcon: Drawable?
        get() = navigationIconView.drawable
        set(value) {
            ensureContainingNavigationIcon()
            navigationIconView.setImageDrawable(value)
        }

    var title: CharSequence?
        get() = titleView.text
        set(value) {
            ensureContainingTitle()
            titleView.text = value
        }

    var subtitle: CharSequence?
        get() = subtitleView.text
        set(value) {
            ensureContainingSubtitle()
            subtitleView.text = value
        }

    val containsTitle: Boolean
        get() = titleView in children

    val containsSubtitle: Boolean
        get() = subtitleView in children

    val containsNavigationIcon: Boolean
        get() = navigationIconView in children

    init {
        elevation = 16F
        minimumWidth = MATCH_PARENT
        minimumHeight = ThemeDimen(mainBarHeightKey).dp
        setBackground(background)
    }

    private fun ensureContainingTitle() {
        if (!containsTitle) {
            addView(titleView)
        }
    }

    private fun ensureContainingSubtitle() {
        if (!containsSubtitle) {
            addView(subtitleView)
        }
    }


    private fun ensureContainingNavigationIcon() {
        if (!containsNavigationIcon) {
            addView(navigationIconView)
        }
    }

    fun setNavigationClickListener(listener: OnClickListener) {
        navigationIconView.setOnClickListener(listener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, background)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec) // requires full width-size
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> min(suggestedMinimumWidth, widthSize)
            else -> widthSize
        } + paddingStart + paddingRight


        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(suggestedMinimumHeight, heightSize)
            else -> suggestedMinimumHeight
        } + paddingTop + paddingBottom

        setMeasuredDimension(width, height)

        val availableWidth = width - horizontalPadding - paddingLeft - paddingRight
        if (containsNavigationIcon) {
            navigationIconView.measure(
                makeMeasureSpec(24.dp, EXACTLY),
                makeMeasureSpec(24.dp, EXACTLY)
            )
        }
        if (containsTitle) {
            titleView.measure(
                makeMeasureSpec(availableWidth, AT_MOST),
                makeMeasureSpec(24.dp, AT_MOST)
            )
        }
        if (containsSubtitle) {
            subtitleView.measure(
                makeMeasureSpec(availableWidth, AT_MOST),
                makeMeasureSpec(20.dp, AT_MOST)
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var widthPosition = horizontalPadding
        var heightPosition = verticalPadding

        if (containsNavigationIcon) {
            navigationIconView.layout(
                widthPosition,
                16.dp,
                (widthPosition + navigationIconView.measuredWidth),
                16.dp + navigationIconView.measuredHeight
            )
            widthPosition += navigationIconView.measuredWidth
        }

        if (containsNavigationIcon) {
            widthPosition += titleHorizontalPadding
        }

        if (containsSubtitle) {
            subtitleView.layout(
                widthPosition,
                heightPosition + titleView.lineHeight,
                widthPosition + subtitleView.measuredWidth,
                heightPosition + titleView.lineHeight + subtitleView.lineHeight
            )
        }

        if (containsTitle) {

            titleView.layout(
                widthPosition,
                heightPosition,
                widthPosition + titleView.measuredWidth,
                heightPosition + titleView.lineHeight
            )
            widthPosition += titleView.measuredWidth
        }


    }

}