package io.github.excu101.vortex.theme.value

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.key.drawerItemHeightKey
import io.github.excu101.vortex.theme.key.drawerItemWidthKey
import io.github.excu101.vortex.theme.key.mainBarHeightKey
import io.github.excu101.vortex.theme.key.maxPathLengthKey
import io.github.excu101.vortex.theme.key.storageListItemHorizontalSubtitlePaddingKey
import io.github.excu101.vortex.theme.key.storageListItemHorizontalTitlePaddingKey
import io.github.excu101.vortex.theme.key.storageListItemLinearHeightDimenKey
import io.github.excu101.vortex.theme.key.storageListItemLinearWidthDimenKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceElevationKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceSelectedElevationKey
import io.github.excu101.vortex.theme.key.storageListLoadingHeightKey
import io.github.excu101.vortex.theme.key.storageListLoadingWidthKey
import io.github.excu101.vortex.theme.key.trailElevationKey
import io.github.excu101.vortex.theme.key.trailHeightKey
import io.github.excu101.vortex.theme.key.trailItemHeightKey
import io.github.excu101.vortex.theme.key.trailItemLeftPaddingKey
import io.github.excu101.vortex.theme.key.trailItemRightPaddingKey
import io.github.excu101.vortex.theme.key.trailWidthKey
import io.github.excu101.vortex.theme.model.Dimen

private inline val MATCH_PARENT
    get() = -1

fun initVortexDimenValues() {
    Theme[mainBarHeightKey] = Dimen(value = MATCH_PARENT)
    Theme[mainBarHeightKey] = Dimen(value = 56)

    Theme[maxPathLengthKey] = Dimen(value = 4096)

    Theme[trailWidthKey] = Dimen(value = MATCH_PARENT) // MATCH_PARENT
    Theme[trailHeightKey] = Dimen(value = 40)

    Theme[trailItemHeightKey] = Dimen(value = 40)
    Theme[trailItemLeftPaddingKey] = Dimen(value = 8)
    Theme[trailItemRightPaddingKey] = Dimen(value = 8)

    Theme[trailElevationKey] = Dimen(value = 4)

    Theme[drawerItemWidthKey] = Dimen(value = MATCH_PARENT)
    Theme[drawerItemHeightKey] = Dimen(value = 48)

    Theme[storageListLoadingWidthKey] = Dimen(value = MATCH_PARENT)
    Theme[storageListLoadingHeightKey] = Dimen(value = 48)

    Theme[storageListItemLinearWidthDimenKey] = Dimen(value = MATCH_PARENT)
    Theme[storageListItemLinearHeightDimenKey] = Dimen(value = 56)
    Theme[storageListItemHorizontalTitlePaddingKey] = Dimen(value = 24)
    Theme[storageListItemHorizontalSubtitlePaddingKey] = Dimen(value = 24)

    Theme[storageListItemSurfaceSelectedElevationKey] = Dimen(value = 8)
    Theme[storageListItemSurfaceElevationKey] = Dimen(value = 2)
}