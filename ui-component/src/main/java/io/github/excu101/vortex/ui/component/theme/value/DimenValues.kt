package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Dimen
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

private inline val MATCH_PARENT
    get() = -1

fun initVortexDimenValues() {
    Theme[mainBarHeightKey] = Dimen(value = 56)

    Theme[maxPathLengthKey] = Dimen(value = 4096)

    Theme[trailWidthKey] = Dimen(value = MATCH_PARENT) // MATCH_PARENT
    Theme[trailHeightKey] = Dimen(value = 48)
    Theme[trailItemHeightKey] = Dimen(value = 48)
    Theme[trailElevationKey] = Dimen(value = 4)

    Theme[drawerItemWidthKey] = Dimen(value = MATCH_PARENT)
    Theme[drawerItemHeightKey] = Dimen(value = 48)

    Theme[storageListItemLinearWidthDimenKey] = Dimen(value = MATCH_PARENT)
    Theme[storageListItemLinearHeightDimenKey] = Dimen(value = 56)
    Theme[storageListItemHorizontalTitlePaddingKey] = Dimen(value = 32)
    Theme[storageListItemHorizontalInfoPaddingKey] = Dimen(value = 32)

    Theme[storageListItemSurfaceSelectedElevationKey] = Dimen(value = 8)
    Theme[storageListItemSurfaceElevationKey] = Dimen(value = 2)
}