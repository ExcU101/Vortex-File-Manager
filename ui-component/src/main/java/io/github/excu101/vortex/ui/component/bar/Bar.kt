package io.github.excu101.vortex.ui.component.bar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList.valueOf
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
import androidx.core.view.children
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.menu.ActionListener
import io.github.excu101.vortex.ui.component.menu.MenuLayout
import io.github.excu101.vortex.ui.component.theme.key.*
import kotlin.math.min

class Bar(context: Context) : ThemeFrameLayout(context), AttachedBehavior {

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
    private val verticalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val behavior = BarBehavior()

    private val desireHeight = ThemeDimen(mainBarHeightKey).dp

    var hideOnScroll: Boolean = true

    private val shape = MaterialShapeDrawable().apply {
        shadowCompatibilityMode = SHADOW_COMPAT_MODE_ALWAYS
        initializeElevationOverlay(context)
        setUseTintColorForShadow(true)
        setTint(ThemeColor(mainBarSurfaceColorKey))
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

    private val menuView = MenuLayout(context).apply {

    }

    var navigationIcon: Drawable?
        get() = navigationIconView.drawable
        set(value) {
            ensureContainingNavigationIcon()
            navigationIconView.setImageDrawable(value)
        }

    var animatesTitleChanges: Boolean = true
    var animatesSubtitleChanges: Boolean = true

    private val titleAnimator = AnimatorSet().apply {
        interpolator = FastOutSlowInInterpolator()
        playTogether(ObjectAnimator.ofFloat(titleView, View.ALPHA, 1F, 0F, 1F))
    }

    private val subtitleAnimator = AnimatorSet().apply {
        interpolator = FastOutSlowInInterpolator()
        playTogether(ObjectAnimator.ofFloat(subtitleView, View.ALPHA, 1F, 0F, 1F))
    }

    private var textWidth = 0


    var title: CharSequence?
        get() = titleView.text
        set(value) {
            ensureContainingTitle()
            if (animatesTitleChanges) {
                titleView.animate().alpha(0F).setDuration(150L).withEndAction {
                    titleView.text = value
                    titleView.animate().alpha(1F).setDuration(150L).start()
                }.start()
            } else {
                titleView.text = value
            }
        }

    var subtitle: CharSequence?
        get() = subtitleView.text
        set(value) {
            if (value == null) {
                removeView(subtitleView)
                return
            }
            ensureContainingSubtitle()
            if (animatesSubtitleChanges) {
                subtitleView.animate().alpha(0F).setDuration(150L).withEndAction {
                    subtitleView.text = value
                    subtitleView.animate().alpha(1F).setDuration(150L).start()
                }.start()
            } else {
                subtitleView.text = value
            }
        }

    val containsTitle: Boolean
        get() = titleView in children

    val containsSubtitle: Boolean
        get() = subtitleView in children

    val containsNavigationIcon: Boolean
        get() = navigationIconView in children

    val containsMenu: Boolean
        get() = menuView in children

    init {
        background = shape
        elevation = 4F.dp
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        MaterialShapeUtils.setElevation(this, elevation)
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

    private fun ensureContainingMenu() {
        if (!containsMenu) {
            addView(menuView)
        }
    }

    fun setNavigationClickListener(listener: OnClickListener) {
        navigationIconView.setOnClickListener(listener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun getBehavior() = behavior

    fun show() {
        behavior.slideUp(child = this)
    }

    fun hide() {
        behavior.slideDown(child = this)
    }

    fun addItem(action: Action) {
        ensureContainingMenu()
        menuView.addItem(action)
    }

    fun replaceItems(actions: List<Action>) {
        ensureContainingMenu()
        menuView.replaceItems(actions)
    }

    fun removeItem(action: Action) {
        ensureContainingMenu()
        menuView.removeItem(action)
    }

    fun addActionListener(listener: ActionListener) {
        menuView.addListener(listener)
    }

    fun removeActionListener(listener: ActionListener) {
        menuView.removeListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = getSize(widthMeasureSpec) // requires full width-size
        val widthMode = getMode(widthMeasureSpec)
        val heightSize = getSize(heightMeasureSpec)
        val heightMode = getMode(heightMeasureSpec)

        val width = when (widthMode) {
            EXACTLY -> widthSize
            AT_MOST -> min(MATCH_PARENT, widthSize)
            else -> MATCH_PARENT
        } + paddingStart + paddingRight


        val height = when (heightMode) {
            EXACTLY -> heightSize
            AT_MOST -> min(desireHeight, heightSize)
            else -> desireHeight
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
        if (containsMenu) {
            textWidth = if (titleView.measuredWidth > subtitleView.measuredWidth) {
                titleView.measuredWidth
            } else {
                subtitleView.measuredWidth
            }
            val paddings = if (containsTitle && containsNavigationIcon) {
                titleHorizontalPadding
            } else {
                0
            }
            val menuWidth = availableWidth - navigationIconView.measuredWidth - textWidth - paddings
            menuView.measure(
                makeMeasureSpec(menuWidth, AT_MOST),
                56.dp
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
                heightPosition,
                widthPosition + navigationIconView.measuredWidth,
                heightPosition + navigationIconView.measuredHeight
            )
            widthPosition += navigationIconView.measuredWidth
        }

        if (containsNavigationIcon) {
            widthPosition += titleHorizontalPadding
        }

        if (containsSubtitle) {
            subtitleView.layout(
                widthPosition,
                8.dp + titleView.lineHeight,
                widthPosition + subtitleView.measuredWidth,
                8.dp + titleView.lineHeight + subtitleView.lineHeight
            )
        }

        if (containsTitle) {
            if (!containsSubtitle) {
                titleView.layout(
                    widthPosition,
                    heightPosition,
                    widthPosition + titleView.measuredWidth,
                    heightPosition + titleView.lineHeight
                )
            } else {
                titleView.layout(
                    widthPosition,
                    8.dp,
                    widthPosition + titleView.measuredWidth,
                    8.dp + titleView.lineHeight
                )

            }
        }

        widthPosition += textWidth

        if (containsMenu) {
            menuView.layout(
                widthPosition,
                0,
                widthPosition + menuView.measuredWidth,
                measuredHeight - paddingBottom
            )
        }
    }

    override fun onChanged() {
        shape.setTint(ThemeColor(mainBarSurfaceColorKey))
        titleView.setTextColor(ThemeColor(mainBarTitleTextColorKey))
        subtitleView.setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
        navigationIconView.setColorFilter(ThemeColor(mainBarNavigationIconTintColorKey))
    }

}