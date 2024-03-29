package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.ThemeColorChangeListener
import io.github.excu101.vortex.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.ThemeUDp
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.foundtation.MarginItemDecoration
import io.github.excu101.vortex.theme.key.trailElevationKey
import io.github.excu101.vortex.theme.key.trailHeightKey
import io.github.excu101.vortex.theme.key.trailSurfaceColorKey
import io.github.excu101.vortex.theme.key.trailWidthKey
import kotlin.math.min

class TrailListView : RecyclerView,
    CoordinatorLayout.AttachedBehavior,
    View.OnApplyWindowInsetsListener, io.github.excu101.vortex.theme.ThemeColorChangeListener {

    constructor(context: Context) : super(context) {
        behavior = TrailBehavior()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    ) {
        behavior = TrailBehavior(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        behavior = TrailBehavior(context, attrs)
    }

    private val behavior: TrailBehavior

    private var trailAdapter = TrailAdapter()

    var isReversed: Boolean
        get() = trailLayoutManager.reverseLayout
        set(value) {
            trailLayoutManager.reverseLayout = value
        }


    private val trailLayoutManager = LinearLayoutManager(context, HORIZONTAL, false)

    private val shape = MaterialShapeDrawable().apply {
        setTint(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.trailSurfaceColorKey))
        shadowCompatibilityMode = SHADOW_COMPAT_MODE_NEVER
        initializeElevationOverlay(context)
    }

    private val desireWidth = ThemeUDp(io.github.excu101.vortex.theme.key.trailWidthKey)
    private val desireHeight = ThemeUDp(io.github.excu101.vortex.theme.key.trailHeightKey)

    init {
        background = shape
        clipToPadding = false
        adapter = trailAdapter
        elevation = io.github.excu101.vortex.theme.ThemeDimen(io.github.excu101.vortex.theme.key.trailElevationKey)
            .toFloat()
        layoutManager = trailLayoutManager
        addItemDecoration(
            MarginItemDecoration(
                horizontal = 4.dp
            )
        )
        setOnApplyWindowInsetsListener(this)
    }

    override fun setBackgroundTintList(tint: ColorStateList?) {
        shape.tintList = tint
    }

    override fun getElevation(): Float = shape.elevation

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)
        shape.elevation = elevation
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        io.github.excu101.vortex.theme.Theme.registerColorChangeListener(listener = this)

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)

        if (parent is ViewGroup) {
            (parent as ViewGroup).clipChildren = false
        }

        if (isAttachedToWindow) {
            requestApplyInsets()
        } else {
            addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            })
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        io.github.excu101.vortex.theme.Theme.unregisterColorChangeListener(listener = this)
    }

    override fun getAdapter(): TrailAdapter = trailAdapter

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthSpec)
        val widthMode = MeasureSpec.getMode(widthSpec)
        val heightSize = MeasureSpec.getSize(heightSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desireWidth, widthSize)
            else -> widthSize
        } + paddingStart + paddingEnd

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desireHeight, heightSize)
            else -> suggestedMinimumHeight
        } + paddingBottom + paddingTop

        setMeasuredDimension(width, height)
    }

    override fun onApplyWindowInsets(view: View, insets: WindowInsets): WindowInsets {
        val compat = toWindowInsetsCompat(insets).getInsets(statusBars())
        view.updatePadding(top = compat.top)
        return insets
    }

    override fun getBehavior() = behavior

    fun slideUp() = behavior.slideUp(view = this)

    fun slideDown() = behavior.slideDown(view = this)

    override fun onColorChanged() {
        shape.setTint(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.trailSurfaceColorKey))
    }

}