package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexDarkColorValues(): Theme {
    Theme[backgroundColorKey] = io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[surfaceColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[accentColorKey] = io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[mainBarActionIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarNavigationIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarSubtitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.LightGray

    Theme[mainBarSurfaceContextualColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarTitleContextualTextColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[mainDrawerTitleColorKey] = io.github.excu101.pluginsystem.model.Color.LightGray
    Theme[mainDrawerBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[mainDrawerSurfaceColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)

    Theme[mainDrawerItemBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[mainDrawerItemIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainDrawerItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[mainDrawerItemSelectedBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4D3062FF)
    Theme[mainDrawerItemIconSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[mainDrawerItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[storageListListBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)

    Theme[storageListAdditionalSurfaceColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[storageListAdditionalTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListAdditionalActionIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[trailSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[trailItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[trailItemArrowTintColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[trailItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x523062FF)

    Theme[storageListWarningBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[storageListWarningIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListWarningTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListWarningActionContentColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[storageListItemSurfaceColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[storageListItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListItemSecondaryTextColorKey] = io.github.excu101.pluginsystem.model.Color.LightGray
    Theme[storageListItemIndexTextColorKey] = io.github.excu101.pluginsystem.model.Color.Gray
    Theme[storageListItemIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListItemIconBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.Gray
    Theme[storageListItemSurfaceRippleColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4DFFFFFF)

    Theme[storageListItemSurfaceSelectedColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4D3062FF)
    Theme[storageListItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemSecondarySelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemIconSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemIconBackgroundSelectedColorKey] = io.github.excu101.pluginsystem.model.Color.DarkGray

    Theme[layoutProgressBarBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[layoutProgressBarTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[layoutProgressActionTintColorKey] = io.github.excu101.pluginsystem.model.Color.White

    return Theme
}