package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Dimen
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.drawerItemHeightKey
import io.github.excu101.vortex.ui.component.theme.key.drawerItemWidthKey
import io.github.excu101.vortex.ui.component.theme.key.mainBarHeightKey
import io.github.excu101.vortex.ui.component.theme.key.maxPathLengthKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemHorizontalInfoPaddingKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemHorizontalTitlePaddingKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemLinearHeightDimenKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemLinearWidthDimenKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceElevationKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemSurfaceSelectedElevationKey
import io.github.excu101.vortex.ui.component.theme.key.trailElevationKey
import io.github.excu101.vortex.ui.component.theme.key.trailHeightKey
import io.github.excu101.vortex.ui.component.theme.key.trailItemHeightKey
import io.github.excu101.vortex.ui.component.theme.key.trailWidthKey

private inline val MATCH_PARENT
    get() = -1

fun initVortexDimenValues() {
    Theme[mainBarHeightKey] = Dimen(value = 56)

    Theme[maxPathLengthKey] = Dimen(value = 4096)

    Theme[trailWidthKey] = Dimen(value = MATCH_PARENT) // MATCH_PARENT
    Theme[trailHeightKey] = Dimen(value = 40)
    Theme[trailItemHeightKey] = Dimen(value = 40)
    Theme[trailElevationKey] = Dimen(value = 4)

    Theme[drawerItemWidthKey] = Dimen(value = MATCH_PARENT)
    Theme[drawerItemHeightKey] = Dimen(value = 48)

    Theme[storageListItemLinearWidthDimenKey] = Dimen(value = MATCH_PARENT)
    Theme[storageListItemLinearHeightDimenKey] = Dimen(value = 56)
    Theme[storageListItemHorizontalTitlePaddingKey] = Dimen(value = 24)
    Theme[storageListItemHorizontalInfoPaddingKey] = Dimen(value = 24)

    Theme[storageListItemSurfaceSelectedElevationKey] = Dimen(value = 8)
    Theme[storageListItemSurfaceElevationKey] = Dimen(value = 2)
}