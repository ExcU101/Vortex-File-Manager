package io.github.excu101.vortex.theme.value.color.ocean

import io.github.excu101.vortex.theme.Theme
import io.github.excu101.vortex.theme.key.accentColorKey
import io.github.excu101.vortex.theme.key.backgroundColorKey
import io.github.excu101.vortex.theme.key.layoutProgressActionTintColorKey
import io.github.excu101.vortex.theme.key.layoutProgressBarBackgroundColorKey
import io.github.excu101.vortex.theme.key.layoutProgressBarTintColorKey
import io.github.excu101.vortex.theme.key.layoutProgressTitleTextColorKey
import io.github.excu101.vortex.theme.key.mainBarActionIconContextualTintColorKey
import io.github.excu101.vortex.theme.key.mainBarActionIconTintColorKey
import io.github.excu101.vortex.theme.key.mainBarNavigationIconContextualTintColorKey
import io.github.excu101.vortex.theme.key.mainBarNavigationIconTintColorKey
import io.github.excu101.vortex.theme.key.mainBarSubtitleTextColorKey
import io.github.excu101.vortex.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.theme.key.mainBarSurfaceContextualColorKey
import io.github.excu101.vortex.theme.key.mainBarTitleContextualTextColorKey
import io.github.excu101.vortex.theme.key.mainBarTitleTextColorKey
import io.github.excu101.vortex.theme.key.mainDrawerBackgroundColorKey
import io.github.excu101.vortex.theme.key.mainDrawerDividerColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemBackgroundColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemIconSelectedTintColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemIconTintColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemSelectedBackgroundColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemTitleSelectedTextColorKey
import io.github.excu101.vortex.theme.key.mainDrawerItemTitleTextColorKey
import io.github.excu101.vortex.theme.key.mainDrawerSurfaceColorKey
import io.github.excu101.vortex.theme.key.mainDrawerTitleColorKey
import io.github.excu101.vortex.theme.key.storageListAdditionalActionIconTintColorKey
import io.github.excu101.vortex.theme.key.storageListAdditionalSurfaceColorKey
import io.github.excu101.vortex.theme.key.storageListAdditionalTitleTextColorKey
import io.github.excu101.vortex.theme.key.storageListBackgroundColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundBookmarkedColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBackgroundSelectedColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconBookmarkedColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconSelectedTintColorKey
import io.github.excu101.vortex.theme.key.storageListItemIconTintColorKey
import io.github.excu101.vortex.theme.key.storageListItemIndexTextColorKey
import io.github.excu101.vortex.theme.key.storageListItemSecondarySelectedTextColorKey
import io.github.excu101.vortex.theme.key.storageListItemSecondaryTextColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceRippleSelectedColorKey
import io.github.excu101.vortex.theme.key.storageListItemSurfaceSelectedColorKey
import io.github.excu101.vortex.theme.key.storageListItemTitleSelectedTextColorKey
import io.github.excu101.vortex.theme.key.storageListItemTitleTextColorKey
import io.github.excu101.vortex.theme.key.storageListWarningActionContentColorKey
import io.github.excu101.vortex.theme.key.storageListWarningBackgroundColorKey
import io.github.excu101.vortex.theme.key.storageListWarningIconTintColorKey
import io.github.excu101.vortex.theme.key.storageListWarningTitleTextColorKey
import io.github.excu101.vortex.theme.key.surfaceColorKey
import io.github.excu101.vortex.theme.key.trailItemArrowSelectedTintColorKey
import io.github.excu101.vortex.theme.key.trailItemArrowTintColorKey
import io.github.excu101.vortex.theme.key.trailItemRippleSelectedTintColorKey
import io.github.excu101.vortex.theme.key.trailItemRippleTintColorKey
import io.github.excu101.vortex.theme.key.trailItemTitleSelectedTextColorKey
import io.github.excu101.vortex.theme.key.trailItemTitleTextColorKey
import io.github.excu101.vortex.theme.key.trailSurfaceColorKey
import io.github.excu101.vortex.theme.model.Color

fun initOceanLightColorValues(): Theme {
    Theme[backgroundColorKey] = Color.White
    Theme[surfaceColorKey] = Color.White
    Theme[accentColorKey] = Color(value = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = Color.White
    Theme[mainBarNavigationIconTintColorKey] = Color.Black
    Theme[mainBarActionIconTintColorKey] = Color.Black
    Theme[mainBarTitleTextColorKey] = Color.Black
    Theme[mainBarSubtitleTextColorKey] = Color.Gray

    Theme[mainBarSurfaceContextualColorKey] = Color(value = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = Color.White
    Theme[mainBarTitleContextualTextColorKey] = Color.White

    Theme[mainDrawerTitleColorKey] = Color.Gray
    Theme[mainDrawerBackgroundColorKey] = Color.White
    Theme[mainDrawerSurfaceColorKey] = Color.White
    Theme[mainDrawerDividerColorKey] = Color.DarkGray

    Theme[mainDrawerItemBackgroundColorKey] = Color.White
    Theme[mainDrawerItemIconTintColorKey] = Color.Black
    Theme[mainDrawerItemTitleTextColorKey] = Color.Black

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(value = 0x4D3062FF)
    Theme[mainDrawerItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[mainDrawerItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)

    Theme[storageListBackgroundColorKey] = Color.White

    Theme[storageListAdditionalSurfaceColorKey] = Color.White
    Theme[storageListAdditionalTitleTextColorKey] = Color.Black
    Theme[storageListAdditionalActionIconTintColorKey] = Color.Black

    Theme[trailSurfaceColorKey] = Color.White
    Theme[trailItemTitleTextColorKey] = Color.Black
    Theme[trailItemArrowTintColorKey] = Color.Black
    Theme[trailItemRippleTintColorKey] = Color(value = 0x52000000)

    Theme[trailItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] = Color(value = 0x523062FF)

    Theme[storageListWarningBackgroundColorKey] = Color.White
    Theme[storageListWarningIconTintColorKey] = Color.Black
    Theme[storageListWarningTitleTextColorKey] = Color.Black
    Theme[storageListWarningActionContentColorKey] = Color(value = 0xFF3062FF)

    Theme[storageListItemSurfaceColorKey] = Color.White
    Theme[storageListItemSurfaceRippleColorKey] = Color(value = 0x52000000)
    Theme[storageListItemTitleTextColorKey] = Color.Black
    Theme[storageListItemSecondaryTextColorKey] = Color.DarkGray
    Theme[storageListItemIndexTextColorKey] = Color.Gray
    Theme[storageListItemIconTintColorKey] = Color.DarkGray
    Theme[storageListItemIconBackgroundColorKey] = Color(0x1F444444)

    Theme[storageListItemSurfaceSelectedColorKey] = Color(value = 0x4D2962FF)
    Theme[storageListItemSurfaceRippleSelectedColorKey] = Color(value = 0x523062FF)
    Theme[storageListItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemSecondarySelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconBackgroundSelectedColorKey] = Color.LightGray

    Theme[storageListItemIconBackgroundBookmarkedColorKey] = Color(value = 0x1F3062FF)
    Theme[storageListItemIconBookmarkedColorKey] = Color(value = 0xFF3062FF)

    Theme[layoutProgressBarBackgroundColorKey] = Color.White
    Theme[layoutProgressBarTintColorKey] = Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.Black
    Theme[layoutProgressActionTintColorKey] = Color.Black

    return Theme
}