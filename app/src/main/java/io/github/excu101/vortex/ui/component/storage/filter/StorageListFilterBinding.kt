package io.github.excu101.vortex.ui.component.storage.filter

import android.animation.StateListAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.ui.component.ViewBinding
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.theme.key.accentColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconSelectedTintColorKey
import io.github.excu101.vortex.ui.component.theme.key.storageListItemIconTintColorKey
import kotlin.random.Random

class StorageListFilterBinding(context: Context) : ViewBinding<LinearLayout> {


    override val root: LinearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL

        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    val handle = View(context).apply {
        background = MaterialShapeDrawable(
            builder().setAllCorners(CornerFamily.ROUNDED, 16F).build()
        )
    }

    val groups = Array(size = 4) {
        createGroup(context)
    }

    init {
        onCreate()
    }


    private val states = ColorStateList(
        arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(),
        ),
        intArrayOf(
            ThemeColor(storageListItemIconSelectedTintColorKey),
            ThemeColor(storageListItemIconTintColorKey),
            Color.TRANSPARENT,
        )
    )

    override fun onCreate() {
        root.addView(
            Space(root.context),
            LinearLayout.LayoutParams(0, with(root) { 8.dp }).apply {
                gravity = Gravity.CENTER
            }
        )
        root.addView(
            handle,
            LinearLayout.LayoutParams(with(handle) { 32.dp }, with(handle) { 4.dp }).apply {
                gravity = Gravity.CENTER
            }
        )

        root.addView(TextView(root.context).apply {
            text = "View"
            setPadding(4.dp)
        }, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })

        groups[0].addView(
            createButton(
                context = groups[0].context,
                title = "Column",
                buttonId = ViewIds.Storage.Sort.ColumnId
            )
        )

        groups[0].addView(
            createButton(
                context = groups[0].context,
                title = "Grid",
                buttonId = ViewIds.Storage.Sort.GridId
            )
        )

        root.addView(
            groups[0]
        )

        root.addView(TextView(root.context).apply {
            text = "Order"
            setPadding(4.dp)
        }, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })

        groups[1].addView(
            createButton(
                context = groups[1].context,
                title = "Ascending",
                buttonId = ViewIds.Storage.Sort.AscendingId
            )
        )

        groups[1].addView(
            createButton(
                context = groups[1].context,
                title = "Descending",
                buttonId = ViewIds.Storage.Sort.DescendingId
            )
        )

        root.addView(
            groups[1]
        )

        root.addView(TextView(root.context).apply {
            text = "Sort"
            setPadding(4.dp)
        }, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })

        groups[2].addView(
            createButton(
                context = groups[2].context,
                title = "Name",
                buttonId = ViewIds.Storage.Sort.NameId
            )
        )

        groups[2].addView(
            createButton(
                context = groups[2].context,
                title = "Path",
                buttonId = ViewIds.Storage.Sort.PathId
            )
        )

        groups[2].addView(
            createButton(
                context = groups[2].context,
                title = "Size",
                buttonId = ViewIds.Storage.Sort.SizeId
            )
        )

        root.addView(
            groups[2]
        )

        root.addView(TextView(root.context).apply {
            text = "Filter"
            setPadding(4.dp)
        }, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })

        groups[3].addView(
            createButton(
                context = groups[3].context,
                title = "Folders",
                buttonId = ViewIds.Storage.Sort.OnlyFoldersId
            )
        )

        groups[3].addView(
            createButton(
                context = groups[3].context,
                title = "Files",
                buttonId = ViewIds.Storage.Sort.OnlyFilesId
            )
        )

        root.addView(
            groups[3]
        )
    }

    private fun createGroup(
        context: Context
    ): MaterialButtonToggleGroup {
        return MaterialButtonToggleGroup(context).apply {
            isSingleSelection = true
            isSelectionRequired = true
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
        }
    }

    private fun createButton(
        context: Context,
        title: String,
        buttonIcon: Drawable? = null,
        buttonId: Int = View.NO_ID
    ): MaterialButton {
        return MaterialButton(context).apply {
            text = title
            id = buttonId
            elevation = 0F
            strokeWidth = 1
            icon = buttonIcon
            cornerRadius = 16.dp
            stateListAnimator = StateListAnimator()
            setTextColor(ThemeColor(accentColorKey))
            rippleColor = ColorStateList.valueOf(0x523062FF)
            strokeColor = states
            iconTint = ColorStateList.valueOf(ThemeColor(accentColorKey))
            setBackgroundColor(Color.TRANSPARENT)
            isChecked = Random.nextBoolean()
        }
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }
}