package io.github.excu101.vortex.ui.component.bar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.ColorStateList.valueOf
import android.graphics.Paint
import android.graphics.drawable.RippleDrawable
import android.text.TextUtils
import android.view.View.MeasureSpec.AT_MOST
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewPropertyAnimator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.textfield.TextInputEditText
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.manager.ui.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ui.component.bar.NavigationIcon.Type.CLOSE
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.menu.MenuLayout
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.themeMeasure


class Bar(context: Context) : ThemeFrameLayout(context) {

    enum class Type {
        TOP,
        BOTTOM
    }

    var type: Type = Type.BOTTOM
        set(value) {
            field = value
            invalidate()
        }

    private val icon = NavigationIcon().apply {
        type = CLOSE
    }

    var isActionModeEnabled: Boolean = false

    private val horizontalPadding = 16.dp
    private val verticalPadding = 16.dp
    private val titleHorizontalPadding = 32.dp
    private val behavior = BarBehavior()

    private val shape = MaterialShapeDrawable().apply {
        shadowCompatibilityMode = SHADOW_COMPAT_MODE_ALWAYS
        paintStyle = Paint.Style.FILL
        initializeElevationOverlay(context)
        setTint(ThemeColor(mainBarSurfaceColorKey))
    }

    private val navigationIconView = ImageView(context).apply {
        isClickable = true
        isFocusable = true
        layoutParams = LayoutParams(24.dp, 24.dp)
        background =
            RippleDrawable(valueOf(ThemeColor(mainBarNavigationIconTintColorKey)), null, null)
        setColorFilter(ThemeColor(mainBarNavigationIconTintColorKey))
        setImageDrawable(icon)
    }

    private val titleView = arrayOfNulls<TextView>(size = 2)

    private val subtitleView = TextView(context).apply {
        textSize = 14F
        setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
    }

    private val menuView = MenuLayout(context).apply {

    }

    private val searchField = TextInputEditText(context).apply {
        hint = "Enter what you want to search"
    }

    val navigationIcon: NavigationIcon
        get() = icon

    var animatesTitleChanges: Boolean = true
    var animatesSubtitleChanges: Boolean = true

    private var textWidth = 0

    var title: CharSequence?
        get() = titleView.getOrNull(0)?.text
        set(value) {
            ensureContainingTitle(0)
            titleView[0]?.text = value
        }

    var subtitle: CharSequence?
        get() = subtitleView.text
        set(value) {
            if (value == null) {
                removeView(subtitleView)
                return
            }
            if (subtitle == value) return
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
        get() = (titleView[0] ?: false) in children

    val containsSubtitle: Boolean
        get() = subtitleView in children

    val containsNavigationIcon: Boolean
        get() = navigationIconView in children

    val containsMenu: Boolean
        get() = menuView in children

    init {
        background = shape
        elevation = 4F.dp
        ensureContainingNavigationIcon()
    }

    override fun setElevation(elevation: Float) {
        shape.elevation = elevation
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        shape.tintList = tint
    }

    override fun getBackgroundTintList(): ColorStateList? {
        return shape.tintList
    }

    private fun createTitle(index: Int) {
        titleView[index] = TextView(context).apply {
            textSize = 18F
            ellipsize = TextUtils.TruncateAt.END
            setLines(1)
            setTextColor(ThemeColor(mainBarTitleTextColorKey))
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        }
    }

    private fun ensureContainingTitle(index: Int) {
        if (!containsTitle) {
            createTitle(index)
            addView(titleView[index])
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

    fun setNavigationClickListener(listener: OnClickListener?) {
        navigationIconView.setOnClickListener(listener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)

        if (parent is ViewGroup) {
            (parent as ViewGroup).clipChildren = false
        }
    }

    fun show() {
        behavior.slideUp(child = this)
    }

    fun hide() {
        behavior.slideDown(child = this)
    }

    fun addItem(action: MenuAction) {
        ensureContainingMenu()
        menuView.addItem(action)
    }

    fun replaceItems(actions: List<MenuAction>) {
        ensureContainingMenu()
        menuView.replaceItems(actions)
    }

    fun removeItem(action: MenuAction) {
        ensureContainingMenu()
        menuView.removeItem(action)
    }

    fun registerListener(listener: MenuActionListener) {
        menuView.addListener(listener)
    }

    fun unregisterListener(listener: MenuActionListener) {
        menuView.removeListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val (width, height) = themeMeasure(
            widthMeasureSpec,
            heightMeasureSpec,
            mainBarWidthKey,
            mainBarHeightKey
        )

        setMeasuredDimension(width, height)

        val availableWidth = width - horizontalPadding - paddingLeft - paddingRight
        if (containsNavigationIcon) {
            measureChild(
                navigationIconView,
                widthMeasureSpec,
                heightMeasureSpec
            )
//            navigationIconView.measure(
//                makeMeasureSpec(24.dp, EXACTLY),
//                makeMeasureSpec(24.dp, EXACTLY)
//            )
        }
        if (containsTitle) {
            titleView.forEach { view ->
                view?.let {
                    if (it.visibility != GONE) {
                        measureChild(
                            it,
                            widthMeasureSpec,
                            heightMeasureSpec
                        )
//                    view?.measure(
//                        makeMeasureSpec(availableWidth, AT_MOST),
//                        makeMeasureSpec(24.dp, AT_MOST)
//                    )
                        it.pivotX = 0F
                        it.pivotY = 1F
                    }
                }

            }
//            titleView.measure(
//                makeMeasureSpec(availableWidth, AT_MOST),
//                makeMeasureSpec(24.dp, AT_MOST)
//            )
        }
        if (containsSubtitle) {
            measureChild(
                subtitleView,
                widthMeasureSpec,
                heightMeasureSpec
            )
//            subtitleView.measure(
//                makeMeasureSpec(availableWidth, AT_MOST),
//                makeMeasureSpec(20.dp, AT_MOST)
//            )
        }
        if (containsMenu) {
//            textWidth = if (titleView.measuredWidth > subtitleView.measuredWidth) {
//                titleView.measuredWidth
//            } else {
//                subtitleView.measuredWidth
//            }
//            val menuWidth = availableWidth - navigationIconView.measuredWidth - textWidth - paddings
            menuView.measure(
                makeMeasureSpec(width, AT_MOST),
                56.dp
            )
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var widthPosition = horizontalPadding
        var textHeightPosition = 8.dp

        if (containsNavigationIcon) {
            navigationIconView.layout(
                widthPosition,
                verticalPadding,
                widthPosition + navigationIconView.measuredWidth,
                verticalPadding + navigationIconView.measuredHeight
            )
            widthPosition += navigationIconView.measuredWidth
        }

        if (containsNavigationIcon) {
            widthPosition += titleHorizontalPadding
        }

        if (!containsSubtitle) {
            textHeightPosition *= 2
        }

        if (containsTitle) {
            titleView.forEach { view ->
                view?.layout(
                    widthPosition,
                    textHeightPosition,
                    widthPosition + view.measuredWidth,
                    textHeightPosition + view.measuredHeight
                )
            }

            textHeightPosition += titleView[0]?.measuredHeight ?: 0
        }

        if (containsSubtitle) {
            subtitleView.layout(
                widthPosition,
                textHeightPosition,
                widthPosition + subtitleView.measuredWidth,
                textHeightPosition + subtitleView.measuredHeight
            )
        }

        widthPosition += textWidth

        if (containsMenu) {
            menuView.layout(
                0,
                0,
                menuView.measuredWidth,
                measuredHeight - paddingBottom
            )
        }
    }

    override fun onColorChanged() {
        shape.setTint(ThemeColor(mainBarSurfaceColorKey))
        titleView[0]?.setTextColor(ThemeColor(mainBarTitleTextColorKey))
        titleView[1]?.setTextColor(ThemeColor(mainBarTitleTextColorKey))
        subtitleView.setTextColor(ThemeColor(mainBarSubtitleTextColorKey))
        navigationIconView.setColorFilter(ThemeColor(mainBarNavigationIconTintColorKey))
    }

    fun setTitleWithAnimation(
        title: CharSequence?,
        isVertical: Boolean = true,
        isReverse: Boolean = false,
        duration: Long = 150L
    ) {
        if (title == this.title) return
        val isCrossfade = title.isNullOrEmpty()

        if (isCrossfade) {

        }

        if (titleView[1]?.parent != null) {
            (titleView[1]?.parent as ViewGroup).removeView(titleView[1])
            titleView[1] = null
        }
        titleView[1] = titleView[0]
        titleView[0] = null
        this.title = title
        titleView[0]?.alpha = 0F


        val a: ViewPropertyAnimator? = titleView[0]?.animate()?.alpha(1f)?.setDuration(
            duration
        )

        if (isVertical) {
            a?.translationY(0F)
        } else {
            a?.translationX(0F)
        }

        a?.start()

        val animator = titleView[1]?.animate()?.alpha(0F)
        if (!isCrossfade) {
            if (isVertical) {
                animator?.translationY(
                    if (isReverse) {
                        (-20F).dp
                    } else {
                        20F.dp
                    }
                )
            }
            else {
                animator?.translationX(
                    if (isReverse) {
                        (-20F).dp
                    } else {
                        20F.dp
                    }
                )
            }
        }
        animator?.setDuration(duration)?.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (titleView[1]?.parent != null) {
                    (titleView[1]?.parent as ViewGroup).removeView(titleView[1])
                    titleView[1] = null
                }

                requestLayout()
            }
        })
        animator?.start()
        requestLayout()
    }
}