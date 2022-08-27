package io.github.excu101.vortex.ui.component.foundtation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class MarginItemDecoration(
    protected val left: Int = 0,
    protected val top: Int = 0,
    protected val right: Int = 0,
    protected val bottom: Int = 0,
) : RecyclerView.ItemDecoration() {

    constructor(vertical: Int = 0, horizontal: Int = 0) : this(
        left = horizontal,
        top = vertical,
        right = horizontal,
        bottom = vertical
    )

    constructor(size: Int = 0) : this(
        vertical = size,
        horizontal = size
    )

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = top
            outRect.bottom = bottom
            outRect.left = left
            outRect.right = right
        }
    }

}