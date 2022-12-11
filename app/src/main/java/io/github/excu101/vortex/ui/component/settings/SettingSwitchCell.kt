package io.github.excu101.vortex.ui.component.settings

import android.content.Context
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import io.github.excu101.pluginsystem.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.data.settings.SwitchItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder

class SettingSwitchCell(context: Context) : ThemeLinearLayout(context),
    ViewHolder.RecyclableView<SwitchItem> {

    private val titleView = TextView(context)
    private val switchView = SwitchMaterial(context)

    var isOptionChecked: Boolean
        get() = switchView.isChecked
        set(value) {
            switchView.isChecked = value
        }

    override fun onBind(item: SwitchItem) {
        titleView.text = item.value
    }

    override fun onUnbind() {
        titleView.text = null
    }

    override fun onChanged() {

    }

}