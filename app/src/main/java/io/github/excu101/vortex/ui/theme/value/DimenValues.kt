package io.github.excu101.vortex.ui.theme.value

import io.github.excu101.pluginsystem.model.Dimen
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.fileItemHeightKey
import io.github.excu101.vortex.ui.theme.key.trailElevationKey
import io.github.excu101.vortex.ui.theme.key.trailHeightKey
import io.github.excu101.vortex.ui.theme.key.trailItemHeightKey

fun initVortexDimenValues() {
    Theme[trailHeightKey] = Dimen(value = 48)
    Theme[trailItemHeightKey] = Dimen(value = 48)
    Theme[trailElevationKey] = Dimen(value = 8)

    Theme[fileItemHeightKey] = Dimen(value = 56)
}