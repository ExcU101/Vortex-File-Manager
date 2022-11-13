package io.github.excu101.vortex.ui.component.theme.value.color.ocean

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

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

    Theme[mainDrawerTitleColorKey] = Color.DarkGray
    Theme[mainDrawerBackgroundColorKey] = Color.White
    Theme[mainDrawerSurfaceColorKey] = Color.White

    Theme[mainDrawerItemBackgroundColorKey] = Color.White
    Theme[mainDrawerItemIconTintColorKey] = Color.Black
    Theme[mainDrawerItemTitleTextColorKey] = Color.Black

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(value = 0x4D3062FF)
    Theme[mainDrawerItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[mainDrawerItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)

    Theme[storageListBackgroundColorKey] = Color.LightGray

    Theme[storageListAdditionalSurfaceColorKey] = Color.White
    Theme[storageListAdditionalTitleTextColorKey] = Color.Black
    Theme[storageListAdditionalActionIconTintColorKey] = Color.Black

    Theme[trailSurfaceColorKey] = Color.White
    Theme[trailItemTitleTextColorKey] = Color.Black
    Theme[trailItemArrowTintColorKey] = Color.Black
    Theme[trailItemRippleTintColorKey] =
        Color(value = 0x52000000)

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
    Theme[storageListItemIconTintColorKey] = Color.White
    Theme[storageListItemIconBackgroundColorKey] = Color.DarkGray

    Theme[storageListItemSurfaceSelectedColorKey] = Color(value = 0x4D2962FF)
    Theme[storageListItemSurfaceRippleSelectedColorKey] = Color(value = 0x523062FF)
    Theme[storageListItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemSecondarySelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconBackgroundSelectedColorKey] = Color.LightGray

    Theme[layoutProgressBarBackgroundColorKey] = Color.White
    Theme[layoutProgressBarTintColorKey] = Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.Black
    Theme[layoutProgressActionTintColorKey] = Color.Black

    return Theme
}