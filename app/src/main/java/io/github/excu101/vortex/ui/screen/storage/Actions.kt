package io.github.excu101.vortex.ui.screen.storage

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ViewIds.Storage.Menu
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.theme.key.fileListMoreActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSearchActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListSortActionTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningFullStorageAccessActionTitleKey
import io.github.excu101.vortex.ui.icon.Icons

object Actions {

    val Create = MenuAction(
        id = ViewIds.Storage.Create.CreateConfirmId,
        title = "Confirm creation",
        icon = Icons.Rounded.Check
    )

    val More = MenuAction(
        id = Menu.MoreId,
        title = ThemeText(fileListMoreActionTitleKey),
        icon = Icons.Rounded.More
    )

    val Tasks = MenuAction(
        id = Menu.TasksId,
        title = "Show tasks",
        icon = Icons.Rounded.Tasks
    )

    val Sort = MenuAction(
        id = Menu.SortId,
        title = ThemeText(fileListSortActionTitleKey),
        icon = Icons.Rounded.Filter
    )

    val Search = MenuAction(
        id = Menu.SearchId,
        title = ThemeText(fileListSearchActionTitleKey),
        icon = Icons.Rounded.Search
    )

    @RequiresApi(Build.VERSION_CODES.R)
    val ProvideFullStorageAccess = MenuAction(
        id = Menu.ProvideFullStorageAccessId,
        title = ThemeText(fileListWarningFullStorageAccessActionTitleKey),
        icon = Icons.Rounded.Check
    )

    val ProvideStorageAccess = MenuAction(
        id = Menu.ProvideStorageAccessId,
        title = ThemeText(fileListWarningFullStorageAccessActionTitleKey),
        icon = Icons.Rounded.Check
    )

    val BarActions = listOf(
        More,
        Search
    )
}