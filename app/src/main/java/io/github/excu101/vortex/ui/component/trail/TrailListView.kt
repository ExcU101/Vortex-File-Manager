package io.github.excu101.vortex.ui.component.trail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowInsets
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.pluginsystem.ui.theme.ThemeDimen
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.foundtation.MarginItemDecoration
import io.github.excu101.vortex.ui.component.theme.key.trailElevationKey
import io.github.excu101.vortex.ui.component.theme.key.trailHeightKey
import io.github.excu101.vortex.ui.component.theme.key.trailSurfaceColorKey
import kotlin.math.min

class TrailListView : RecyclerView,
    CoordinatorLayout.AttachedBehavior,
    View.OnApplyWindowInsetsListener {

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
        initializeElevationOverlay(context)
        setTint(ThemeColor(trailSurfaceColorKey))
    }

    override fun getElevation(): Float = shape.elevation

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)

        MaterialShapeUtils.setElevation(this, elevation)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        MaterialShapeUtils.setParentAbsoluteElevation(this, shape)
    }

    override fun getAdapter(): TrailAdapter = trailAdapter

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(trailLayoutManager)
    }

    override fun getLayoutManager() = trailLayoutManager

    init {
        minimumWidth = MATCH_PARENT
        minimumHeight = ThemeDimen(trailHeightKey).dp
        background = shape
        elevation = ThemeDimen(trailElevationKey).toFloat()
        clipToPadding = false
        adapter = trailAdapter
        layoutManager = trailLayoutManager
        addItemDecoration(MarginItemDecoration(
            horizontal = 4.dp
        ))
        setOnApplyWindowInsetsListener(this)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthSpec)
        val widthMode = MeasureSpec.getMode(widthSpec)
        val heightSize = MeasureSpec.getSize(heightSpec)
        val heightMode = MeasureSpec.getMode(heightSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(suggestedMinimumWidth, widthSize)
            else -> widthSize
        } + paddingStart + paddingEnd

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(suggestedMinimumHeight, heightSize)
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

}