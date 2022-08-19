package io.github.excu101.vortex.ui.theme

import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.ui.theme.key.*

fun defaultLightTheme(): Theme {
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
    Theme[mainDrawerActionIconTintColorKey] = Color.Black
    Theme[mainDrawerActionTitleTextColorKey] = Color.Black

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(value = 0x4D3062FF)
    Theme[mainDrawerActionIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[mainDrawerActionTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)

    Theme[fileAdditionalSurfaceColorKey] = Color.White
    Theme[fileAdditionalTitleTextColorKey] = Color.Black
    Theme[fileAdditionalActionIconTintColorKey] = Color.Black

    Theme[trailSurfaceColorKey] = Color.White
    Theme[trailItemTitleTextColorKey] = Color.Black
    Theme[trailItemArrowTintColorKey] = Color.Black
    Theme[trailItemRippleTintColorKey] = Color(value = 0x52000000)

    Theme[trailItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] = Color(value = 0x523062FF)

    Theme[fileWarningBackgroundColorKey] = Color.White
    Theme[fileWarningIconTintColorKey] = Color.Black
    Theme[fileWarningTitleTextColorKey] = Color.Black
    Theme[fileWarningActionContentColorKey] = Color(value = 0xFF3062FF)

    Theme[fileItemSurfaceColorKey] = Color.White
    Theme[fileItemTitleTextColorKey] = Color.Black
    Theme[fileItemSecondaryTextColorKey] = Color.DarkGray
    Theme[fileItemIndexTextColorKey] = Color.Gray
    Theme[fileItemIconTintColorKey] = Color.White
    Theme[fileItemIconBackgroundColorKey] = Color.DarkGray

    Theme[fileItemSurfaceSelectedColorKey] = Color(value = 0x4D2962FF)
    Theme[fileItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemSecondarySelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemIconBackgroundSelectedColorKey] = Color.LightGray

    Theme[layoutProgressBarBackgroundColorKey] = Color.White
    Theme[layoutProgressBarTintColorKey] = Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.Black
    Theme[layoutProgressActionTintColorKey] = Color.Black

    return Theme
}