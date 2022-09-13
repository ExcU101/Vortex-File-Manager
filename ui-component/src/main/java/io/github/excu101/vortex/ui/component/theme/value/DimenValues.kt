package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Dimen
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexDimenValues() {
    Theme[mainBarHeightKey] = Dimen(value = 56)

    Theme[trailHeightKey] = Dimen(value = 48)
    Theme[trailItemHeightKey] = Dimen(value = 48)
    Theme[trailElevationKey] = Dimen(value = 4)

    Theme[fileItemHeightKey] = Dimen(value = 56)
    Theme[fileItemTitleLeftPaddingKey] = Dimen(value = 32)
    Theme[fileItemInfoLeftPaddingKey] = Dimen(value = 32)
}