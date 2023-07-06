package io.github.excu101.vortex.ui.screen.storage

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.excu101.vortex.theme.ThemeText
import io.github.excu101.vortex.ViewIds.Storage.Menu
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSearchActionTitleKey
import io.github.excu101.vortex.theme.key.fileListSortActionTitleKey
import io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessActionTitleKey
import io.github.excu101.vortex.ui.icon.Icons

object Actions {

    val More = MenuAction(
        id = Menu.MoreId,
        title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListMoreActionTitleKey),
        icon = Icons.Rounded.More
    )

    val Tasks = MenuAction(
        id = Menu.TasksId,
        title = "Show tasks",
        icon = Icons.Rounded.Tasks
    )

    inline val Sort
        get() = MenuAction(
            id = Menu.SortId,
            title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListSortActionTitleKey),
            icon = Icons.Rounded.Filter
        )


    inline val Search
        get() = MenuAction(
            id = Menu.SearchId,
            title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListSearchActionTitleKey),
            icon = Icons.Rounded.Search
        )

    inline val ProvideFullStorageAccess
        @RequiresApi(Build.VERSION_CODES.R)
        get() = MenuAction(
            id = Menu.ProvideFullStorageAccessId,
            title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessActionTitleKey),
            icon = Icons.Rounded.Check
        )

    inline val ProvideStorageAccess
        get() = MenuAction(
            id = Menu.ProvideStorageAccessId,
            title = io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessActionTitleKey),
            icon = Icons.Rounded.Check
        )

    inline val BarActions
        get() = listOf(
            More,
            Search
        )
}