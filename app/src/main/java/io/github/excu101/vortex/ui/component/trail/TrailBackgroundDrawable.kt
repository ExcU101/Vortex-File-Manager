package io.github.excu101.vortex.ui.component.trail

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable

class TrailBackgroundDrawable : RippleDrawable {

    var isSelected: Boolean = false


    constructor(
        color: ColorStateList,
        content: Drawable?,
        mask: Drawable? = null,
    ) : super(
        color,
        content,
        mask
    ) {

    }
}
