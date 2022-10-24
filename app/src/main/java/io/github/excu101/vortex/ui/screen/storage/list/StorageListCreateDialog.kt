package io.github.excu101.vortex.ui.screen.storage.list

import android.content.Context
import android.os.Bundle
import com.google.android.material.shape.CornerFamily.ROUNDED
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.sheet.BottomSheetDialog
import io.github.excu101.vortex.ui.component.storage.create.StorageListCreateLayout
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

class StorageListCreateDialog(
    context: Context,
) : BottomSheetDialog(context) {

    private val root = StorageListCreateLayout(context).apply {
        background = MaterialShapeDrawable(
            builder()
                .setTopLeftCorner(ROUNDED, 16F)
                .setTopRightCorner(ROUNDED, 16F)
                .build()
        ).apply {
            setTint(ThemeColor(mainDrawerBackgroundColorKey))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent(view = root)
    }

    fun onPreShow(
        name: String? = null,
        path: String? = null,
    ): StorageListCreateDialog {
        if (name != null) {
            root.name = name
        }
        if (path != null) {
            root.path = path
        }

        return this
    }

}