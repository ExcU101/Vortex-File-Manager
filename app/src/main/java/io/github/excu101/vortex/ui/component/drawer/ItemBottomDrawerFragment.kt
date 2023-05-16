package io.github.excu101.vortex.ui.component.drawer

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel.builder
import io.github.excu101.manager.ui.theme.ThemeColor
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.theme.key.mainDrawerBackgroundColorKey

class ItemBottomDrawerFragment(
    context: Context,
    vararg types: Pair<Int, ViewHolderFactory<Item<*>>>,
) : BottomSheetDialog(context) {

    interface ItemsProvider {
        val items: List<Item<*>>
    }

    companion object {
        const val Tag = "BottomActionDrawer"
    }

    private val adapter = ItemAdapter(
        *types
    )

    private var binding: BottomSheetDrawerBinding = BottomSheetDrawerBinding(context)

    private val background = MaterialShapeDrawable(
        builder().setTopLeftCorner(
            CornerFamily.ROUNDED,
            16F
        ).setTopRightCorner(
            CornerFamily.ROUNDED,
            16F
        ).build()
    ).apply {
        setTint(ThemeColor(mainDrawerBackgroundColorKey))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.onCreate()
        binding.root.background = background
        setContentView(binding.root)

        binding.apply {
            root.background = background

            items.adapter = adapter
        }
    }

    fun register(listener: ItemViewListener<Item<*>>): ItemBottomDrawerFragment {
        adapter.register(listener)
        return this
    }

    fun withItems(items: List<Item<*>>): ItemBottomDrawerFragment {
        adapter.replace(items = items)
        return this
    }

    fun withItems(provider: ItemsProvider): ItemBottomDrawerFragment {
        adapter.replace(provider.items)
        return this
    }

}