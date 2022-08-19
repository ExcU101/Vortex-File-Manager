package io.github.excu101.vortex.ui.view.trail

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.WindowInsetsCompat.toWindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.data.Dimen
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.trailElevationKey
import io.github.excu101.vortex.ui.theme.key.trailSurfaceColorKey
import io.github.excu101.vortex.ui.view.dp
import io.github.excu101.vortex.ui.view.foundtation.MarginItemDecoration

class TrailView : RecyclerView,
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
        setTint(Theme<Int, Color>(trailSurfaceColorKey))
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

    init {
        background = shape
        elevation = Theme<Int, Dimen>(trailElevationKey).toFloat()
        clipToPadding = false
        adapter = trailAdapter
        layoutManager = trailLayoutManager
        addItemDecoration(MarginItemDecoration(
            left = 4.dp,
            right = 4.dp
        ))
        setOnApplyWindowInsetsListener(this)
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