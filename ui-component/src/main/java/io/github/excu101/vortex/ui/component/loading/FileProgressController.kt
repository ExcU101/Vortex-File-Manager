package io.github.excu101.vortex.ui.component.loading

import androidx.annotation.FloatRange
import io.github.excu101.vortex.ui.component.bar.Bar

interface FileProgressController {
    @setparam:FloatRange(from = 0.0, to = 1.0)
    @get:FloatRange(from = 0.0, to = 1.0)
    var progress: Float
    var unit: String?
    var title: String?

    fun show(bar: Bar? = null)

    fun hide(bar: Bar? = null)
}