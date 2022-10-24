package io.github.excu101.vortex.ui.component.theme.value

import io.github.excu101.pluginsystem.model.Color
import io.github.excu101.pluginsystem.ui.theme.Theme
import io.github.excu101.vortex.ui.component.theme.key.*

fun initVortexLightColorValues(): Theme {
    Theme[backgroundColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[surfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[accentColorKey] = io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[mainBarSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarNavigationIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[mainBarActionIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[mainBarTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[mainBarSubtitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Gray

    Theme[mainBarSurfaceContextualColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF212121)
    Theme[mainBarActionIconContextualTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarNavigationIconContextualTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainBarTitleContextualTextColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[mainDrawerTitleColorKey] = io.github.excu101.pluginsystem.model.Color.DarkGray
    Theme[mainDrawerBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainDrawerSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White

    Theme[mainDrawerItemBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[mainDrawerItemIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[mainDrawerItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black

    Theme[mainDrawerItemSelectedBackgroundColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4D3062FF)
    Theme[mainDrawerItemIconSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[mainDrawerItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[storageListListBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.LightGray

    Theme[storageListAdditionalSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListAdditionalTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[storageListAdditionalActionIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black

    Theme[trailSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[trailItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[trailItemArrowTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[trailItemRippleTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x52000000)

    Theme[trailItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[trailItemArrowSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[trailItemRippleSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x523062FF)

    Theme[storageListWarningBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListWarningIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[storageListWarningTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[storageListWarningActionContentColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)

    Theme[storageListItemSurfaceColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListItemTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[storageListItemSecondaryTextColorKey] = io.github.excu101.pluginsystem.model.Color.DarkGray
    Theme[storageListItemIndexTextColorKey] = io.github.excu101.pluginsystem.model.Color.Gray
    Theme[storageListItemIconTintColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[storageListItemIconBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.DarkGray
    Theme[storageListItemSurfaceRippleColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4D000000)

    Theme[storageListItemSurfaceSelectedColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0x4D2962FF)
    Theme[storageListItemTitleSelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemSecondarySelectedTextColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemIconSelectedTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[storageListItemIconBackgroundSelectedColorKey] = io.github.excu101.pluginsystem.model.Color.LightGray

    Theme[layoutProgressBarBackgroundColorKey] = io.github.excu101.pluginsystem.model.Color.White
    Theme[layoutProgressBarTintColorKey] =
        io.github.excu101.pluginsystem.model.Color(value = 0xFF3062FF)
    Theme[layoutProgressTitleTextColorKey] = io.github.excu101.pluginsystem.model.Color.Black
    Theme[layoutProgressActionTintColorKey] = io.github.excu101.pluginsystem.model.Color.Black

    return Theme
}