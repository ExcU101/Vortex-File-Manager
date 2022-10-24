package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Dimen
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexDimenValues() {
    Theme[mainBarHeightKey] = io.github.excu101.pluginsystem.model.Dimen(value = 56)

    Theme[maxPathLengthKey] = io.github.excu101.pluginsystem.model.Dimen(value = 4096)

    Theme[trailWidthKey] = io.github.excu101.pluginsystem.model.Dimen(value = -1) // MATCH_PARENT
    Theme[trailHeightKey] = io.github.excu101.pluginsystem.model.Dimen(value = 48)
    Theme[trailItemHeightKey] = io.github.excu101.pluginsystem.model.Dimen(value = 48)
    Theme[trailElevationKey] = io.github.excu101.pluginsystem.model.Dimen(value = 4)

    Theme[fileItemHeightKey] = io.github.excu101.pluginsystem.model.Dimen(value = 56)
    Theme[fileItemTitleLeftPaddingKey] = io.github.excu101.pluginsystem.model.Dimen(value = 32)
    Theme[fileItemInfoLeftPaddingKey] = io.github.excu101.pluginsystem.model.Dimen(value = 32)
}