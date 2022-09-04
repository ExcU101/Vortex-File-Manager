package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexDarkColorValues(): Theme {
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
    Theme[mainDrawerActionIconTintColorKey] = Color.White
    Theme[mainDrawerActionTitleTextColorKey] = Color.White

    Theme[mainDrawerItemSelectedBackgroundColorKey] = Color(value = 0x4D3062FF)
    Theme[mainDrawerActionIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[mainDrawerActionTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)

    Theme[fileAdditionalSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[fileAdditionalTitleTextColorKey] = Color.White
    Theme[fileAdditionalActionIconTintColorKey] = Color.White

    Theme[trailSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[trailItemTitleTextColorKey] = Color.White
    Theme[trailItemArrowTintColorKey] = Color.White

    Theme[trailItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] = Color(value = 0x523062FF)

    Theme[fileWarningBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[fileWarningIconTintColorKey] = Color.White
    Theme[fileWarningTitleTextColorKey] = Color.White
    Theme[fileWarningActionContentColorKey] = Color(value = 0xFF3062FF)

    Theme[fileItemSurfaceColorKey] = Color(value = 0xFF212121)
    Theme[fileItemTitleTextColorKey] = Color.White
    Theme[fileItemSecondaryTextColorKey] = Color.LightGray
    Theme[fileItemIndexTextColorKey] = Color.Gray
    Theme[fileItemIconTintColorKey] = Color.White
    Theme[fileItemIconBackgroundColorKey] = Color.Gray

    Theme[fileItemSurfaceSelectedColorKey] = Color(value = 0x4D3062FF)
    Theme[fileItemTitleSelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemSecondarySelectedTextColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemIconSelectedTintColorKey] = Color(value = 0xFF3062FF)
    Theme[fileItemIconBackgroundSelectedColorKey] = Color.DarkGray

    Theme[layoutProgressBarBackgroundColorKey] = Color(value = 0xFF212121)
    Theme[layoutProgressBarTintColorKey] = Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = Color.White
    Theme[layoutProgressActionTintColorKey] = Color.White

    return Theme
}