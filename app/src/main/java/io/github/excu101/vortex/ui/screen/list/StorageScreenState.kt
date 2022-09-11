package io.github.excu101.vortex.ui.screen.list

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import io.github.excu101.vortex.ui.component.list.adapter.Item

data class StorageScreenState(
    val data: List<Item<*>> = listOf(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val isWarning: Boolean = false,
    val warningIcon: Drawable? = null,
    val warningMessage: String? = null,
    val requiresPermissions: Boolean = false,
    @RequiresApi(30)
    val requiresAllFilePermission: Boolean = false,
) {

    companion object {
        fun content(data: List<Item<*>>) = StorageScreenState(
            data = data,
            isLoading = false,
            loadingTitle = null,
            isWarning = false,
            warningIcon = null,
            warningMessage = null,
            requiresAllFilePermission = false,
            requiresPermissions = false
        )

        fun loading(title: String? = null) = StorageScreenState(
            data = listOf(),
            isLoading = true,
            loadingTitle = title,
            isWarning = false,
            warningIcon = null,
            warningMessage = null,
            requiresAllFilePermission = false,
            requiresPermissions = false
        )
    }

}

data class StorageScreenSideEffect(
    val showDrawer: Boolean = false,
    val drawerActions: List<Item<*>> = listOf(),
    val message: String? = null,
    val messageDuration: Int = Snackbar.LENGTH_SHORT,
    val messageActionTitle: String? = null,
    val messageAction: (View.OnClickListener)? = null,
)
