package io.github.excu101.vortex.ui.screen.storage.list.page.list.operation.observer

import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.theme.key.fileListOperationDeleteItemPerformedTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListOperationDeleteItemTitleKey
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageViewModel

internal class ViewModelDeleteObserver(
    private val viewModel: StorageListPageViewModel,
    private val onComplete: () -> Unit,
) : FileOperationObserver {

    override fun onAction(value: Path) {
        viewModel.message(
            text = FormatterThemeText(
                key = fileListOperationDeleteItemTitleKey,
                value.getName().toString()
            )
        )
    }

    override fun onError(value: Throwable) {
        viewModel.message(text = value.message!!)
    }

    override fun onComplete() {
        viewModel.message(text = ThemeText(
            key = fileListOperationDeleteItemPerformedTitleKey
        ))
        onComplete.invoke()
    }

}