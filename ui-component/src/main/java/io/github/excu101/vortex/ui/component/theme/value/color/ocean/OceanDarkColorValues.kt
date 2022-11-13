package io.github.excu101.vortex.ui.component.theme.value.color.ocean

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initOceanDarkColorValues(): Theme {
    Theme[backgroundColorKey] = Color(value = 0xFF212121)
    Theme[surfaceColorKey] = Color.Black
    Theme[accentColorKey] = Color(value = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[mainBarActionIconTintColorKey] = Color.White
    Theme[mainBarNavigationIconTintColorKey] = Color.White
    Theme[mainBarTitleTextColorKey] = Color.White
    Theme[mainBarSubtitleTextColorKey] = Color.LightGray

    Theme[mainBarSurfaceContextualColorKey] = Color(value = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = Color.White
    Theme[mainBarTitleContextualTextColorKey] = Color.White

    Theme[mainDrawerTitleColorKey] = Color.LightGray
    Theme[mainDrawerBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[mainDrawerSurfaceColorKey] = Color(value = 0xFF212121)

    Theme[mainDrawerItemBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[mainDrawerItemIconTintColorKey] = Color.White
    Theme[mainDrawerItemTitleTextColorKey] = Color.White

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(value = 0x4D3062FF)
    Theme[mainDrawerItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[mainDrawerItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)

    Theme[storageListBackgroundColorKey] = Color(value = 0xFF212121)

    Theme[storageListAdditionalSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[storageListAdditionalTitleTextColorKey] = Color.White
    Theme[storageListAdditionalActionIconTintColorKey] = Color.White

    Theme[trailSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[trailItemTitleTextColorKey] = Color.White
    Theme[trailItemArrowTintColorKey] = Color.White

    Theme[trailItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] = Color(value = 0x523062FF)

    Theme[storageListWarningBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[storageListWarningIconTintColorKey] = Color.White
    Theme[storageListWarningTitleTextColorKey] = Color.White
    Theme[storageListWarningActionContentColorKey] = Color(value = 0xFF3062FF)

    Theme[storageListItemSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[storageListItemSurfaceRippleColorKey] = Color(value = 0x52000000)
    Theme[storageListItemTitleTextColorKey] = Color.White
    Theme[storageListItemSecondaryTextColorKey] = Color.LightGray
    Theme[storageListItemIndexTextColorKey] = Color.Gray
    Theme[storageListItemIconTintColorKey] = Color.White
    Theme[storageListItemIconBackgroundColorKey] = Color.Gray

    Theme[storageListItemSurfaceSelectedColorKey] = Color(value = 0x4D3062FF)
    Theme[storageListItemSurfaceRippleSelectedColorKey] = Color(value = 0x523062FF)
    Theme[storageListItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemSecondarySelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[storageListItemIconBackgroundSelectedColorKey] = Color.DarkGray

    Theme[layoutProgressBarBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[layoutProgressBarTintColorKey] = Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.White
    Theme[layoutProgressActionTintColorKey] = Color.White

    return Theme
}