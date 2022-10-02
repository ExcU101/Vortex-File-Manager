package io.github.excu101.vortex.ui.screen.list

import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.list.adapter.Item

data class StorageScreenState(
    val data: List<Item<*>> = listOf(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val isWarning: Boolean = false,
    val warningIcon: Drawable? = null,
    val warningMessage: String? = null,
    val warningActions: List<Action> = listOf(),
) {

    companion object {
        fun loading(title: String? = null) = StorageScreenState(
            data = listOf(),
            isLoading = true,
            loadingTitle = title,
            isWarning = false,
            warningIcon = null,
            warningMessage = null,
            warningActions = listOf(),
        )
    }

}

data class StorageScreenContentState(
    val content: List<PathItem> = listOf(),
    val selected: List<PathItem> = listOf(),
    val currentSelectedTrail: Int = 0,
    val trail: List<PathItem> = listOf(),
) {
    val isItemTrailFirst
        get() = currentSelectedTrail == 0

    val isItemTrailLast
        get() = currentSelectedTrail == trail.lastIndex

    val isEmpty
        get() = content.isEmpty()

    val isSelectedEmpty
        get() = selected.isEmpty()

    val isSelectedContainsContent
        get() = selected.containsAll(content)

    val selectedCount
        get() = selected.count()
}

enum class DialogType {
    CREATE
}

data class StorageScreenSideEffect(
    val showDrawer: Boolean = false,
    val drawerActions: List<Item<*>> = listOf(),
    val message: String? = null,
    val messageDuration: Int = Snackbar.LENGTH_SHORT,
    val messageActionTitle: String? = null,
    val messageAction: (View.OnClickListener)? = null,
)
