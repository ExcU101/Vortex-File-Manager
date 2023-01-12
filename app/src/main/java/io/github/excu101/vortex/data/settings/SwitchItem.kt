package io.github.excu101.vortex.data.settings

import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.Preferences
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.dsl.ItemScope
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.SuperItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.settings.SettingSwitchCell
import io.github.excu101.vortex.utils.SwitchItem

class SwitchItem(
    override val title: String,
    override val value: Boolean,
    override val key: Preferences.Key<Boolean>
) : Item<Boolean>, BooleanSettingsItem {

    override val id: Long
        get() = key.name.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.SwitchItem

    companion object : ViewHolderFactory<SwitchItem> {
        override fun produceView(parent: ViewGroup): View = SettingSwitchCell(parent.context)
    }
}

fun ItemScope<SuperItem>.switchItem(
    title: String,
    value: Boolean,
    key: Preferences.Key<Boolean>
) {
    add(SwitchItem(title, value, key))
}