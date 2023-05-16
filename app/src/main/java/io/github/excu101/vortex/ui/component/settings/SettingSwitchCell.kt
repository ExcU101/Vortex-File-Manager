package io.github.excu101.vortex.ui.component.settings

import android.content.Context
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import io.github.excu101.manager.ui.theme.widget.ThemeLinearLayout
import io.github.excu101.vortex.data.settings.SwitchSettingsItem
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder.RecyclableView

class SettingSwitchCell(context: Context) : ThemeLinearLayout(context),
    RecyclableView<SwitchSettingsItem> {

    private val titleView = TextView(context).apply {
        textSize = 16F
    }
    private val switchView = SwitchMaterial(context)

    var isOptionChecked: Boolean
        get() = switchView.isChecked
        set(value) {
            switchView.isChecked = value
        }

    init {
        orientation = HORIZONTAL
        addView(titleView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        addView(switchView)
    }

    override fun onBind(item: SwitchSettingsItem) {
        titleView.text = item.title
        switchView.isChecked = item.value
    }

    override fun onUnbind() {
        titleView.text = null
        switchView.isChecked = false
    }

    override fun onBindListener(listener: OnClickListener) {
        setOnClickListener(listener)
        switchView.setOnClickListener(listener)
    }

    override fun onColorChanged() {

    }

}